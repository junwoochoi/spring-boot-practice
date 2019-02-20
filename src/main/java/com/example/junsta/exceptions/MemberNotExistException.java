package com.example.junsta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, code = HttpStatus.BAD_REQUEST, reason = "memeber not exist")
public class MemberNotExistException extends RuntimeException {
    public MemberNotExistException() {
        super("Can't find Member");
    }

    public MemberNotExistException(String message) {
        super(message);
    }
}
