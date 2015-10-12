package net.sblundy.experiments.ember.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 */
@Entity
public class AuthorRecord {
    @Id
    private UUID authorId;
    @Basic(optional = false)
    private String fullName;

    public AuthorRecord() {
    }

    public AuthorRecord(UUID authorId, String fullName) {
        this.authorId = authorId;
        this.fullName = fullName;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
