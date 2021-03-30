package com.skripsi.skripsiservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class FileNotUploadedException extends RuntimeException {
    public FileNotUploadedException(String message){
        super(message);
    }
}
