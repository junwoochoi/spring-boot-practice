package com.example.junsta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, code = HttpStatus.BAD_REQUEST, reason = "parameter is not valid")
public class BadParametersException extends RuntimeException {
    public BadParametersException() {
        super("parameter is not valid");
    }

    public BadParametersException(String message) {
        super(message);
    }
}
