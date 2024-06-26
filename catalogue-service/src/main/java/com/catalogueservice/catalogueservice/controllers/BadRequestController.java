package com.catalogueservice.catalogueservice.controllers;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.catalogueservice.catalogueservice.exceptions.UniquenessViolationException;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class BadRequestController {

    private final MessageSource messageSource;

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(
        BindException exception,
        Locale locale
    ) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, 
                        this.messageSource.getMessage(
                                "catalogue.errors.400.title", new Object[0], locale)); 
        
        problemDetail.setProperty("errors", 
                exception.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .toList());

        return ResponseEntity.badRequest()
                .body(problemDetail);
    }

    @ExceptionHandler(UniquenessViolationException.class)
    public ResponseEntity<ProblemDetail> handleUniquenessViolationException(
        Exception exception,
        Locale locale
    ) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, 
                        this.messageSource.getMessage(
                                "catalogue.errors.400.title.unique", new Object[0], locale));

        problemDetail.setProperty("errors", 
                exception.getMessage());

        return ResponseEntity.badRequest()
                .body(problemDetail);
    }
}
