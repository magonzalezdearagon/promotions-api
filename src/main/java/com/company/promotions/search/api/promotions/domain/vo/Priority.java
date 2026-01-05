package com.company.promotions.search.api.promotions.domain.vo;

import com.company.promotions.search.api.promotions.domain.exceptions.InvalidPriorityException;

public record Priority(Integer value) implements Comparable<Priority> {

    public Priority {
        if (value == null) {
            throw new InvalidPriorityException("Priority cannot be null");
        }
        if (value < 0) {
            throw new InvalidPriorityException("Priority must be non-negative");
        }
    }

    public static Priority of(Integer value) {
        return new Priority(value);
    }

    @Override
    public int compareTo(Priority other) {
        return this.value.compareTo(other.value);
    }
}
