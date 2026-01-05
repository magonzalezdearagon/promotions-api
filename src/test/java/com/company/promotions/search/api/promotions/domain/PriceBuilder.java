package com.company.promotions.search.api.promotions.domain;

import com.company.promotions.search.api.promotions.domain.vo.BrandId;
import com.company.promotions.search.api.promotions.domain.vo.DateRange;
import com.company.promotions.search.api.promotions.domain.vo.Money;
import com.company.promotions.search.api.promotions.domain.vo.PriceListId;
import com.company.promotions.search.api.promotions.domain.vo.Priority;
import com.company.promotions.search.api.promotions.domain.vo.ProductId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public class PriceBuilder {

    private BrandId brandId = BrandId.of(1L);
    private DateRange dateRange = DateRange.of(
            LocalDateTime.of(2020, 6, 14, 0, 0, 0),
            LocalDateTime.of(2020, 12, 31, 23, 59, 59)
    );
    private PriceListId priceListId = PriceListId.of(1L);
    private ProductId productId = ProductId.of(35455L);
    private Priority priority = Priority.of(0);
    private Money money = Money.of(BigDecimal.valueOf(35.50), Currency.getInstance("EUR"));

    public static PriceBuilder aPrice() {
        return new PriceBuilder();
    }

    public PriceBuilder withBrandId(Long brandId) {
        this.brandId = BrandId.of(brandId);
        return this;
    }

    public PriceBuilder withBrandId(BrandId brandId) {
        this.brandId = brandId;
        return this;
    }

    public PriceBuilder withDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        this.dateRange = DateRange.of(startDate, endDate);
        return this;
    }

    public PriceBuilder withDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
        return this;
    }

    public PriceBuilder withPriceListId(Long priceListId) {
        this.priceListId = PriceListId.of(priceListId);
        return this;
    }

    public PriceBuilder withPriceListId(PriceListId priceListId) {
        this.priceListId = priceListId;
        return this;
    }

    public PriceBuilder withProductId(Long productId) {
        this.productId = ProductId.of(productId);
        return this;
    }

    public PriceBuilder withProductId(ProductId productId) {
        this.productId = productId;
        return this;
    }

    public PriceBuilder withPriority(Integer priority) {
        this.priority = Priority.of(priority);
        return this;
    }

    public PriceBuilder withPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public PriceBuilder withMoney(BigDecimal amount, String currencyCode) {
        this.money = Money.of(amount, currencyCode);
        return this;
    }

    public PriceBuilder withMoney(BigDecimal amount, Currency currency) {
        this.money = Money.of(amount, currency);
        return this;
    }

    public PriceBuilder withMoney(Money money) {
        this.money = money;
        return this;
    }

    public Price build() {
        return new Price(brandId, dateRange, priceListId, productId, priority, money);
    }
}
