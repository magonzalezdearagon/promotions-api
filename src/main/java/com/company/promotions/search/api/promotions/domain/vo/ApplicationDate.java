package com.company.promotions.search.api.promotions.domain.vo;

import java.time.LocalDateTime;
import java.util.Objects;

public record ApplicationDate(LocalDateTime value) {

    public ApplicationDate {
        Objects.requireNonNull(value, "Application date cannot be null");
    }

    public static ApplicationDate of(LocalDateTime value) {
        return new ApplicationDate(value);
    }

    public static ApplicationDate now() {
        return new ApplicationDate(LocalDateTime.now());
    }

    public boolean isBetween(LocalDateTime start, LocalDateTime end) {
        return !value.isBefore(start) && !value.isAfter(end);
    }
}