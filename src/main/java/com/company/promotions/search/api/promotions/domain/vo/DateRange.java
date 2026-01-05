package com.company.promotions.search.api.promotions.domain.vo;

import com.company.promotions.search.api.promotions.domain.exceptions.InvalidDateRangeException;

import java.time.LocalDateTime;
import java.util.Objects;

public record DateRange(LocalDateTime startDate, LocalDateTime endDate) {

    public DateRange {
        if (startDate == null) {
            throw new InvalidDateRangeException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new InvalidDateRangeException("End date cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidDateRangeException("End date cannot be before start date");
        }
    }

    public static DateRange of(LocalDateTime startDate, LocalDateTime endDate) {
        return new DateRange(startDate, endDate);
    }

    public boolean contains(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime, "DateTime cannot be null");
        return !dateTime.isBefore(startDate) && !dateTime.isAfter(endDate);
    }

    public java.time.Duration duration() {
        return java.time.Duration.between(startDate, endDate);
    }
}
