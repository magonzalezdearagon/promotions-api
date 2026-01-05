package com.company.promotions.search.api.promotions.domain.vo;

import com.company.promotions.search.api.promotions.domain.exceptions.InvalidPriceListIdException;

public record PriceListId(Long value) {

    public PriceListId {
        if (value == null) {
            throw new InvalidPriceListIdException("Price list ID cannot be null");
        }
        if (value <= 0) {
            throw new InvalidPriceListIdException("Price list ID must be positive");
        }
    }

    public static PriceListId of(Long value) {
        return new PriceListId(value);
    }
}
