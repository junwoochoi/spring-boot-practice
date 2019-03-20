package com.example.junsta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, code = HttpStatus.BAD_REQUEST, reason = "Account email is already in used")
public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException() {
        super("Account email is already in used");
    }

    public AccountAlreadyExistException(String message) {
        super(message);
    }
}
