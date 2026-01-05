package com.company.promotions.search.api.promotions.infrastructure.rest;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Response containing the applicable price information")
public record FindProductsPriceResponse(
        @Schema(description = "Product identifier", example = "35455")
        Long productId,

        @Schema(description = "Brand identifier", example = "1")
        Long brandId,

        @Schema(description = "Price list/tariff identifier", example = "2")
        Integer priceList,

        @Schema(description = "Start date of the price validity period", example = "2020-06-14T15:00:00")
        LocalDateTime startDate,

        @Schema(description = "End date of the price validity period", example = "2020-06-14T18:30:00")
        LocalDateTime endDate,

        @Schema(description = "Final applicable price", example = "25.45")
        BigDecimal price
) {
}
