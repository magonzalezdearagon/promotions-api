package com.company.promotions.search.api.promotions.domain;

import com.company.promotions.search.api.promotions.domain.vo.BrandId;
import com.company.promotions.search.api.promotions.domain.vo.ProductId;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {

    List<Price> findApplicablePrices(ProductId productId, BrandId brandId, LocalDateTime applicationDate);
}
