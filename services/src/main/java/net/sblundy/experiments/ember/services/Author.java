package net.sblundy.experiments.ember.services;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 */
public class Author extends ResourceSupport {

    private final String fullName;

    @JsonCreator
    public Author(@JsonProperty("fullName") String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
