package com.company.promotions.search.api.promotions.infrastructure.persistence;

import com.company.promotions.search.api.promotions.domain.Price;
import com.company.promotions.search.api.promotions.domain.PriceRepository;
import com.company.promotions.search.api.promotions.domain.vo.BrandId;
import com.company.promotions.search.api.promotions.domain.vo.ProductId;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PriceRepositoryImpl implements PriceRepository {

    private final JpaPriceRepository jpaPriceRepository;

    public PriceRepositoryImpl(JpaPriceRepository jpaPriceRepository) {
        this.jpaPriceRepository = jpaPriceRepository;
    }

    @Override
    public List<Price> findApplicablePrices(ProductId productId, BrandId brandId, LocalDateTime applicationDate) {
        return jpaPriceRepository.findApplicablePrices(productId.value(), brandId.value(), applicationDate)
                .stream()
                .map(PriceEntity::toDomain)
                .toList();
    }
}
