package com.company.promotions.search.api.promotions.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApplicablePriceDto(
        Long productId,
        Long brandId,
        Integer priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price
) {
}