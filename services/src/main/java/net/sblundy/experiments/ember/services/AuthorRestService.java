package net.sblundy.experiments.ember.services;

import net.sblundy.experiments.ember.data.AuthorRepository;
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
public class AuthorRestService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorRestService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/author",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public List<Author> getAuthors() {
        List<Author> authors = new ArrayList<>();
        for (AuthorRecord authorRecord : authorRepository.findAll()) {
            authors.add(createAuthor(authorRecord));
        }
        return authors;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/author/{authorId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Author getAuthor(@PathVariable("authorId") UUID authorId) {
        AuthorRecord authorRecord = authorRepository.findOne(authorId);
        if (null == authorRecord) {
            throw new AuthorNotFoundException();
        }
        return createAuthor(authorRecord);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/author",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<Void> addAuthor(@RequestBody Author author) {
        if(null != authorRepository.findByFullName(author.getFullName())) {
            throw new DuplicateEntryException();
        }
        UUID authorId = UUID.randomUUID();
        authorRepository.save(new AuthorRecord(authorId, author.getFullName()));
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, linkTo(methodOn(AuthorRestService.class).getAuthor(authorId)).toUri().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    private Author createAuthor(AuthorRecord authorRecord) {
        Author author = new Author(authorRecord.getFullName());
        author.add(linkTo(methodOn(AuthorRestService.class).getAuthor(authorRecord.getAuthorId())).withSelfRel());
        author.add(linkTo(methodOn(BookRestService.class).getAuthorBooks(authorRecord.getAuthorId())).withRel("books"));
        return author;
    }


}
