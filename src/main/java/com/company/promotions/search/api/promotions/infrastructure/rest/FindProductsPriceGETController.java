package com.company.promotions.search.api.promotions.infrastructure.rest;

import com.company.promotions.search.api.promotions.application.ApplicablePriceDto;
import com.company.promotions.search.api.promotions.application.FindProductsPriceBetweenDatesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prices")
@Tag(name = "Prices", description = "Operations for querying applicable prices")
public class FindProductsPriceGETController {

    private final FindProductsPriceBetweenDatesUseCase findProductsPriceBetweenDatesUseCase;

    public FindProductsPriceGETController(FindProductsPriceBetweenDatesUseCase findProductsPriceBetweenDatesUseCase) {
        this.findProductsPriceBetweenDatesUseCase = findProductsPriceBetweenDatesUseCase;
    }

    @Operation(
            summary = "Find applicable price",
            description = "Returns the applicable price for a product and brand at a given date. " +
                    "When multiple prices apply, the one with the smallest date range is returned."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Applicable price found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FindProductsPriceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing or invalid API key"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No applicable price found for the given parameters"
            )
    })
    @GetMapping
    public ResponseEntity<FindProductsPriceResponse> findPrice(
            @Parameter(hidden = true) @Valid FindProductsPriceRequest request) {
        return findProductsPriceBetweenDatesUseCase.execute(
                        request.applicationDate(),
                        request.productId(),
                        request.brandId())
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private FindProductsPriceResponse toResponse(ApplicablePriceDto dto) {
        return new FindProductsPriceResponse(
                dto.productId(),
                dto.brandId(),
                dto.priceList(),
                dto.startDate(),
                dto.endDate(),
                dto.price()
        );
    }
}
