package com.company.promotions.search.api.promotions.application;

import com.company.promotions.search.api.promotions.domain.Price;
import com.company.promotions.search.api.promotions.domain.PriceRepository;
import com.company.promotions.search.api.promotions.domain.vo.BrandId;
import com.company.promotions.search.api.promotions.domain.vo.ProductId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FindProductsPriceBetweenDatesUseCase {

    private final PriceRepository priceRepository;

    public FindProductsPriceBetweenDatesUseCase(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public Optional<ApplicablePriceDto> execute(LocalDateTime applicationDate, Long productId, Long brandId) {
        List<Price> applicablePrices = priceRepository.findApplicablePrices(
                ProductId.of(productId),
                BrandId.of(brandId),
                applicationDate);

        return Price.selectMostSpecific(applicablePrices)
                .map(this::toDto);
    }

    private ApplicablePriceDto toDto(Price price) {
        return new ApplicablePriceDto(
                price.getProductId().value(),
                price.getBrandId().value(),
                price.getPriceListId().value().intValue(),
                price.getDateRange().startDate(),
                price.getDateRange().endDate(),
                price.getMoney().amount()
        );
    }
}
