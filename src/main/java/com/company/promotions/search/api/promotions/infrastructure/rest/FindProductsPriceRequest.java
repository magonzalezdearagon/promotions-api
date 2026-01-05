package com.company.promotions.search.api.promotions.infrastructure.rest;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FindProductsPriceRequest(
        @Parameter(description = "Date and time for which to find the applicable price", example = "2020-06-14T10:00:00", required = true)
        @NotNull LocalDateTime applicationDate,

        @Parameter(description = "Product identifier", example = "35455", required = true)
        @NotNull Long productId,

        @Parameter(description = "Brand identifier (e.g., 1 for ZARA)", example = "1", required = true)
        @NotNull Long brandId
) {
}
