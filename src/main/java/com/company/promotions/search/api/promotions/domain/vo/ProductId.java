package com.company.promotions.search.api.promotions.domain.vo;

import com.company.promotions.search.api.promotions.domain.exceptions.InvalidProductIdException;

public record ProductId(Long value) {

    public ProductId {
        if (value == null) {
            throw new InvalidProductIdException("Product ID cannot be null");
        }
        if (value <= 0) {
            throw new InvalidProductIdException("Product ID must be positive");
        }
    }

    public static ProductId of(Long value) {
        return new ProductId(value);
    }
}