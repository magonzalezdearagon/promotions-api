package com.company.promotions.search.api.promotions.domain;

import com.company.promotions.search.api.promotions.domain.vo.BrandId;
import com.company.promotions.search.api.promotions.domain.vo.DateRange;
import com.company.promotions.search.api.promotions.domain.vo.Money;
import com.company.promotions.search.api.promotions.domain.vo.PriceListId;
import com.company.promotions.search.api.promotions.domain.vo.Priority;
import com.company.promotions.search.api.promotions.domain.vo.ProductId;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Price {

    private final BrandId brandId;
    private final DateRange dateRange;
    private final PriceListId priceListId;
    private final ProductId productId;
    private final Priority priority;
    private final Money money;

    public Price(BrandId brandId, DateRange dateRange, PriceListId priceListId,
                 ProductId productId, Priority priority, Money money) {
        this.brandId = Objects.requireNonNull(brandId, "Brand ID cannot be null");
        this.dateRange = Objects.requireNonNull(dateRange, "Date range cannot be null");
        this.priceListId = Objects.requireNonNull(priceListId, "Price list ID cannot be null");
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.priority = Objects.requireNonNull(priority, "Priority cannot be null");
        this.money = Objects.requireNonNull(money, "Money cannot be null");
    }

    public boolean isApplicableAt(LocalDateTime dateTime) {
        return dateRange.contains(dateTime);
    }

    /**
     * Selects the most specific price from a list of applicable prices.
     * Business rule: When multiple prices apply for the same date, product and brand,
     * the price with the smallest date range (most specific) takes precedence.
     *
     * @param prices list of applicable prices
     * @return the most specific price, or empty if the list is empty
     */
    public static Optional<Price> selectMostSpecific(List<Price> prices) {
        return prices.stream()
                .min(Comparator.comparing(price -> price.getDateRange().duration()));
    }

    public BrandId getBrandId() {
        return brandId;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public PriceListId getPriceListId() {
        return priceListId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public Priority getPriority() {
        return priority;
    }

    public Money getMoney() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(brandId, price.brandId) &&
                Objects.equals(dateRange, price.dateRange) &&
                Objects.equals(priceListId, price.priceListId) &&
                Objects.equals(productId, price.productId) &&
                Objects.equals(priority, price.priority) &&
                Objects.equals(money, price.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandId, dateRange, priceListId, productId, priority, money);
    }

    @Override
    public String toString() {
        return "Price{" +
                "brandId=" + brandId +
                ", dateRange=" + dateRange +
                ", priceListId=" + priceListId +
                ", productId=" + productId +
                ", priority=" + priority +
                ", money=" + money +
                '}';
    }
}
