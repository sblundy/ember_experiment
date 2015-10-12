package net.sblundy.experiments.ember.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 */
public class Book extends ResourceSupport {
    private final String name;
    private final String authorName;

    @JsonCreator
    public Book(@JsonProperty("name") String name, @JsonProperty("authorName") String authorName) {
        this.name = name;
        this.authorName = authorName;
    }

    public String getName() {
        return name;
    }

    public String getAuthorName() {
        return authorName;
    }
}
