package com.company.promotions.search.api.promotions.domain.vo;

import com.company.promotions.search.api.promotions.domain.exceptions.InvalidBrandIdException;

public record BrandId(Long value) {

    public BrandId {
        if (value == null) {
            throw new InvalidBrandIdException("Brand ID cannot be null");
        }
        if (value <= 0) {
            throw new InvalidBrandIdException("Brand ID must be positive");
        }
    }

    public static BrandId of(Long value) {
        return new BrandId(value);
    }
}