package net.sblundy.experiments.ember.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Item already exists")
public class DuplicateEntryException extends RuntimeException {
}
