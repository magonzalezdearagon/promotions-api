package com.company.promotions.search.api.promotions.infrastructure.rest.advices;

import com.company.promotions.search.api.promotions.domain.exceptions.InvalidPriceListIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidPriceListIdExceptionHandler {

    @ExceptionHandler(InvalidPriceListIdException.class)
    public ProblemDetail handle(InvalidPriceListIdException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Invalid Price List ID");
        return problemDetail;
    }
}
