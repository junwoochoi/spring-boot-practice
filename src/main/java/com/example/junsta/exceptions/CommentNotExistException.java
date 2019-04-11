package com.example.junsta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, code = HttpStatus.BAD_REQUEST, reason = "comment not exist")
public class CommentNotExistException extends RuntimeException{
    public CommentNotExistException() {
        super("Can't find comment");
    }

    public CommentNotExistException(String message) {
        super(message);
    }
}
