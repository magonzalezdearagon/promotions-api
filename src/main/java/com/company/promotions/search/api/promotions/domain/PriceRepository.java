package com.company.promotions.search.api.promotions.domain;

import com.company.promotions.search.api.promotions.domain.vo.BrandId;
import com.company.promotions.search.api.promotions.domain.vo.ProductId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PriceRepository {

    List<Price> findByProductIdAndBrandId(ProductId productId, BrandId brandId);

    List<Price> findApplicablePrices(ProductId productId, BrandId brandId, LocalDateTime applicationDate);

    Optional<Price> findApplicablePrice(ProductId productId, BrandId brandId, LocalDateTime applicationDate);
}
