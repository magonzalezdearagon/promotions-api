package com.company.promotions.search.api.promotions.infrastructure.rest.advices;

import com.company.promotions.search.api.promotions.domain.exceptions.InvalidBrandIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidBrandIdExceptionHandler {

    @ExceptionHandler(InvalidBrandIdException.class)
    public ProblemDetail handle(InvalidBrandIdException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Invalid Brand ID");
        return problemDetail;
    }
}
