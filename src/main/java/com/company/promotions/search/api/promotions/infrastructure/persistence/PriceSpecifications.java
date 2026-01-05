package com.company.promotions.search.api.promotions.infrastructure.persistence;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class PriceSpecifications {

    private PriceSpecifications() {
    }

    public static Specification<PriceEntity> hasProductId(Long productId) {
        return (root, query, cb) ->
                productId == null ? null : cb.equal(root.get("productId"), productId);
    }

    public static Specification<PriceEntity> hasBrandId(Long brandId) {
        return (root, query, cb) ->
                brandId == null ? null : cb.equal(root.get("brandId"), brandId);
    }

    public static Specification<PriceEntity> hasPriceList(Integer priceList) {
        return (root, query, cb) ->
                priceList == null ? null : cb.equal(root.get("priceList"), priceList);
    }

    public static Specification<PriceEntity> hasCurrency(String currency) {
        return (root, query, cb) ->
                currency == null ? null : cb.equal(root.get("currency"), currency);
    }

    public static Specification<PriceEntity> hasPriorityGreaterThanOrEqual(Integer minPriority) {
        return (root, query, cb) ->
                minPriority == null ? null : cb.greaterThanOrEqualTo(root.get("priority"), minPriority);
    }

    public static Specification<PriceEntity> hasPriceBetween(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    public static Specification<PriceEntity> isActiveAt(LocalDateTime dateTime) {
        return (root, query, cb) -> {
            if (dateTime == null) return null;
            return cb.and(
                    cb.lessThanOrEqualTo(root.get("startDate"), dateTime),
                    cb.greaterThanOrEqualTo(root.get("endDate"), dateTime)
            );
        };
    }

    public static Specification<PriceEntity> startsAfter(LocalDateTime dateTime) {
        return (root, query, cb) ->
                dateTime == null ? null : cb.greaterThan(root.get("startDate"), dateTime);
    }

    public static Specification<PriceEntity> startsBefore(LocalDateTime dateTime) {
        return (root, query, cb) ->
                dateTime == null ? null : cb.lessThan(root.get("startDate"), dateTime);
    }

    public static Specification<PriceEntity> endsAfter(LocalDateTime dateTime) {
        return (root, query, cb) ->
                dateTime == null ? null : cb.greaterThan(root.get("endDate"), dateTime);
    }

    public static Specification<PriceEntity> endsBefore(LocalDateTime dateTime) {
        return (root, query, cb) ->
                dateTime == null ? null : cb.lessThan(root.get("endDate"), dateTime);
    }

    public static Specification<PriceEntity> overlapsWithPeriod(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null || end == null) return null;
            return cb.and(
                    cb.lessThanOrEqualTo(root.get("startDate"), end),
                    cb.greaterThanOrEqualTo(root.get("endDate"), start)
            );
        };
    }

    public static Specification<PriceEntity> orderByPriorityDesc() {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(root.get("priority")));
            return null;
        };
    }
}