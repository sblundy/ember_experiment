package net.sblundy.experiments.ember.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Book author not found")
public class BookAuthorNotFoundException extends RuntimeException {

}
