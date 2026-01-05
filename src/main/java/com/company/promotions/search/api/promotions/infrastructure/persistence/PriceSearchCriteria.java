package com.company.promotions.search.api.promotions.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceSearchCriteria(
        Long productId,
        Long brandId,
        Integer priceList,
        String currency,
        Integer minPriority,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        LocalDateTime activeAt,
        LocalDateTime startsAfter,
        LocalDateTime startsBefore,
        LocalDateTime endsAfter,
        LocalDateTime endsBefore,
        LocalDateTime periodStart,
        LocalDateTime periodEnd
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long productId;
        private Long brandId;
        private Integer priceList;
        private String currency;
        private Integer minPriority;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private LocalDateTime activeAt;
        private LocalDateTime startsAfter;
        private LocalDateTime startsBefore;
        private LocalDateTime endsAfter;
        private LocalDateTime endsBefore;
        private LocalDateTime periodStart;
        private LocalDateTime periodEnd;

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder brandId(Long brandId) {
            this.brandId = brandId;
            return this;
        }

        public Builder priceList(Integer priceList) {
            this.priceList = priceList;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder minPriority(Integer minPriority) {
            this.minPriority = minPriority;
            return this;
        }

        public Builder minPrice(BigDecimal minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public Builder maxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder activeAt(LocalDateTime activeAt) {
            this.activeAt = activeAt;
            return this;
        }

        public Builder startsAfter(LocalDateTime startsAfter) {
            this.startsAfter = startsAfter;
            return this;
        }

        public Builder startsBefore(LocalDateTime startsBefore) {
            this.startsBefore = startsBefore;
            return this;
        }

        public Builder endsAfter(LocalDateTime endsAfter) {
            this.endsAfter = endsAfter;
            return this;
        }

        public Builder endsBefore(LocalDateTime endsBefore) {
            this.endsBefore = endsBefore;
            return this;
        }

        public Builder periodStart(LocalDateTime periodStart) {
            this.periodStart = periodStart;
            return this;
        }

        public Builder periodEnd(LocalDateTime periodEnd) {
            this.periodEnd = periodEnd;
            return this;
        }

        public PriceSearchCriteria build() {
            return new PriceSearchCriteria(
                    productId, brandId, priceList, currency, minPriority,
                    minPrice, maxPrice, activeAt, startsAfter, startsBefore,
                    endsAfter, endsBefore, periodStart, periodEnd
            );
        }
    }
}