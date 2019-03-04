package com.example.junsta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, code = HttpStatus.BAD_REQUEST, reason = "post not exist")
public class PostNotExistException extends RuntimeException {
    public PostNotExistException() {
        super("Can't find post");
    }

    public PostNotExistException(String message) {
        super(message);
    }
}
