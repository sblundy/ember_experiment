package net.sblundy.experiments.ember.model;


import javax.persistence.*;
import java.util.UUID;

/**
 */
@Entity
public class BookRecord {
    @Id
    private UUID bookId;

    @Basic(optional = false)
    private String title;

    @ManyToOne(optional = false)
    private AuthorRecord author;

    public BookRecord() {
    }

    public BookRecord(UUID bookId, String title, AuthorRecord author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
    }

    public UUID getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AuthorRecord getAuthor() {
        return author;
    }

    public void setAuthor(AuthorRecord author) {
        this.author = author;
    }
}
