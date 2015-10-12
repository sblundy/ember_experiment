package net.sblundy.experiments.ember.data;

import net.sblundy.experiments.ember.model.BookRecord;
import net.sblundy.experiments.ember.model.AuthorRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 */
public interface BookRepository extends CrudRepository<BookRecord, UUID> {
    BookRecord findByAuthorAndTitle(AuthorRecord author, String title);

    Iterable<BookRecord> findByAuthor(AuthorRecord author);
}
