package com.example.junsta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, code = HttpStatus.UNAUTHORIZED, reason = "doesn't have authorities")
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("doesn't have authorities");
    }
}
