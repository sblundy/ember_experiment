package net.sblundy.experiments.ember.services;

import net.sblundy.experiments.ember.data.BookRepository;
import net.sblundy.experiments.ember.data.AuthorRepository;
import net.sblundy.experiments.ember.model.BookRecord;
import net.sblundy.experiments.ember.model.AuthorRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 */
@Controller
public class BookRestService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookRestService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/book/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Book getBook(@PathVariable("bookId") UUID bookId) {
        BookRecord bookRecord = bookRepository.findOne(bookId);
        if (null == bookRecord) {
            throw new BookNotFoundException();
        }
        Book book = new Book(bookRecord.getTitle(), bookRecord.getAuthor().getFullName());
        book.add(linkTo(methodOn(AuthorRestService.class).getAuthor(bookRecord.getAuthor().getAuthorId())).withRel("author"));
        return book;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/author/{authorId}/books",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<Book> getAuthorBooks(@PathVariable("authorId") UUID authorId) {
        final AuthorRecord author = authorRepository.findOne(authorId);
        if (null == author) {
            throw new AuthorNotFoundException();
        }
        List<Book> books = new ArrayList<>();
        for (BookRecord bookRecord : bookRepository.findByAuthor(author)) {
            Book book = new Book(bookRecord.getTitle(), bookRecord.getAuthor().getFullName());
            book.add(linkTo(methodOn(AuthorRestService.class).getAuthor(bookRecord.getAuthor().getAuthorId())).withRel("author"));
            books.add(book);
        }
        return books;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/book",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> addBook(@RequestBody Book book) {
        AuthorRecord authorRecord = authorRepository.findByFullName(book.getAuthorName());
        if (authorRecord == null) {
            throw new BookAuthorNotFoundException();
        }
        UUID bookId = UUID.randomUUID();
        if(null != bookRepository.findByAuthorAndTitle(authorRecord, book.getName())) {
            throw new DuplicateEntryException();
        }
        BookRecord newBookRecord = new BookRecord(bookId, book.getName(), authorRecord);
        bookRepository.save(newBookRecord);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, linkTo(methodOn(BookRestService.class).getBook(bookId)).toUri().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

}
