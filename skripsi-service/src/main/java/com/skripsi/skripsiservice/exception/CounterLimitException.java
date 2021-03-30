package com.skripsi.skripsiservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class CounterLimitException extends RuntimeException{

    public CounterLimitException(String exception){
        super(exception);
    }

}
