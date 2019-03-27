package com.example.junsta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, code = HttpStatus.BAD_REQUEST, reason = "UploadedImageId is not exist")
public class UploadedImageNotExistException extends RuntimeException {
    public UploadedImageNotExistException() {
        super("UploadedImageId is not exist");
    }
}
