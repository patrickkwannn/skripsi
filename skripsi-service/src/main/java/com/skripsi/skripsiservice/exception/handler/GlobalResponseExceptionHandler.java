package com.skripsi.skripsiservice.exception.handler;

import com.skripsi.skripsiservice.domain.GeneralResponse;
import com.skripsi.skripsiservice.exception.CounterLimitException;
import com.skripsi.skripsiservice.exception.FileNotUploadedException;
import com.skripsi.skripsiservice.exception.PostNotFoundException;
import com.skripsi.skripsiservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

public class GlobalResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = UserNotFoundException.class)
    public final ResponseEntity<GeneralResponse> handleUserNotFoundException(
            UserNotFoundException e, WebRequest webRequest) {

        return new ResponseEntity<GeneralResponse>(
                new GeneralResponse.Builder()
                        .withMessage(HttpStatus.NOT_FOUND.toString())
                        .withPath(webRequest.getDescription(false))
                        .withResponse(e.getMessage())
                        .withDate(new Date())
                        .build(),
                HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = UserNotFoundException.class)
    public final ResponseEntity<GeneralResponse> handlePostNotFoundException(
            PostNotFoundException e, WebRequest webRequest) {

        return new ResponseEntity<GeneralResponse>(
                new GeneralResponse.Builder()
                        .withMessage(HttpStatus.NOT_FOUND.toString())
                        .withPath(webRequest.getDescription(false))
                        .withResponse(e.getMessage())
                        .withDate(new Date())
                        .build(),
                HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(value = UserNotFoundException.class)
    public final ResponseEntity<GeneralResponse> handleFileNotUploadedException(
            FileNotUploadedException e, WebRequest webRequest) {

        return new ResponseEntity<GeneralResponse>(
                new GeneralResponse.Builder()
                        .withMessage(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                        .withPath(webRequest.getDescription(false))
                        .withResponse(e.getMessage())
                        .withDate(new Date())
                        .build(),
                HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(value = UserNotFoundException.class)
    public final ResponseEntity<GeneralResponse> handleCounterLimitException(
            CounterLimitException e, WebRequest webRequest) {

        return new ResponseEntity<GeneralResponse>(
                new GeneralResponse.Builder()
                        .withMessage(HttpStatus.EXPECTATION_FAILED.toString())
                        .withPath(webRequest.getDescription(false))
                        .withResponse(e.getMessage())
                        .withDate(new Date())
                        .build(),
                HttpStatus.OK);
    }


}
