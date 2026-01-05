package com.company.promotions.search.api.promotions.infrastructure.persistence;

import com.company.promotions.search.api.promotions.domain.Price;
import com.company.promotions.search.api.promotions.domain.PriceRepository;
import com.company.promotions.search.api.promotions.domain.vo.BrandId;
import com.company.promotions.search.api.promotions.domain.vo.ProductId;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PriceRepositoryImpl implements PriceRepository {

    private final JpaPriceRepository jpaPriceRepository;

    public PriceRepositoryImpl(JpaPriceRepository jpaPriceRepository) {
        this.jpaPriceRepository = jpaPriceRepository;
    }

    @Override
    public List<Price> findByProductIdAndBrandId(ProductId productId, BrandId brandId) {
        return jpaPriceRepository.findByProductIdAndBrandId(productId.value(), brandId.value())
                .stream()
                .map(PriceEntity::toDomain)
                .toList();
    }

    @Override
    public List<Price> findApplicablePrices(ProductId productId, BrandId brandId, LocalDateTime applicationDate) {
        return jpaPriceRepository.findApplicablePrices(productId.value(), brandId.value(), applicationDate)
                .stream()
                .map(PriceEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Price> findApplicablePrice(ProductId productId, BrandId brandId, LocalDateTime applicationDate) {
        return jpaPriceRepository.findApplicablePrices(productId.value(), brandId.value(), applicationDate)
                .stream()
                .findFirst()
                .map(PriceEntity::toDomain);
    }
}
