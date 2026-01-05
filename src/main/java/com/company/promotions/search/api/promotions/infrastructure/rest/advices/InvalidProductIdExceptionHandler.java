package com.company.promotions.search.api.promotions.infrastructure.rest.advices;

import com.company.promotions.search.api.promotions.domain.exceptions.InvalidProductIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidProductIdExceptionHandler {

    @ExceptionHandler(InvalidProductIdException.class)
    public ProblemDetail handle(InvalidProductIdException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Invalid Product ID");
        return problemDetail;
    }
}
