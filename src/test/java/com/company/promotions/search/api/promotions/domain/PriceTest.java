package com.company.promotions.search.api.promotions.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.company.promotions.search.api.promotions.domain.PriceBuilder.aPrice;
import static org.assertj.core.api.Assertions.assertThat;

class PriceTest {

    @Nested
    @DisplayName("selectMostSpecific")
    class SelectMostSpecificTest {

        @Test
        @DisplayName("Should return empty when list is empty")
        void shouldReturnEmptyWhenListIsEmpty() {
            Optional<Price> result = Price.selectMostSpecific(Collections.emptyList());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return the only price when list has one element")
        void shouldReturnOnlyPriceWhenListHasOneElement() {
            Price price = aPrice()
                    .withPriceListId(1L)
                    .withMoney(BigDecimal.valueOf(35.50), "EUR")
                    .build();

            Optional<Price> result = Price.selectMostSpecific(List.of(price));

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(price);
        }

        @Test
        @DisplayName("Should return price with smallest date range when multiple prices exist")
        void shouldReturnPriceWithSmallestDateRange() {
            // Wide range: 6 months (June 14 to December 31)
            Price wideRangePrice = aPrice()
                    .withPriceListId(1L)
                    .withDateRange(
                            LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                            LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                    .withMoney(BigDecimal.valueOf(35.50), "EUR")
                    .build();

            // Narrow range: 3.5 hours (15:00 to 18:30)
            Price narrowRangePrice = aPrice()
                    .withPriceListId(2L)
                    .withDateRange(
                            LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                            LocalDateTime.of(2020, 6, 14, 18, 30, 0))
                    .withMoney(BigDecimal.valueOf(25.45), "EUR")
                    .build();

            // Medium range: 11 hours (00:00 to 11:00)
            Price mediumRangePrice = aPrice()
                    .withPriceListId(3L)
                    .withDateRange(
                            LocalDateTime.of(2020, 6, 15, 0, 0, 0),
                            LocalDateTime.of(2020, 6, 15, 11, 0, 0))
                    .withMoney(BigDecimal.valueOf(30.50), "EUR")
                    .build();

            List<Price> prices = List.of(wideRangePrice, narrowRangePrice, mediumRangePrice);

            Optional<Price> result = Price.selectMostSpecific(prices);

            assertThat(result).isPresent();
            assertThat(result.get().getPriceListId().value()).isEqualTo(2L);
            assertThat(result.get().getMoney().amount()).isEqualByComparingTo(BigDecimal.valueOf(25.45));
        }

        @Test
        @DisplayName("Should return first price when multiple prices have same duration")
        void shouldReturnFirstPriceWhenSameDuration() {
            Price price1 = aPrice()
                    .withPriceListId(1L)
                    .withDateRange(
                            LocalDateTime.of(2020, 6, 14, 10, 0, 0),
                            LocalDateTime.of(2020, 6, 14, 12, 0, 0))
                    .withMoney(BigDecimal.valueOf(10.00), "EUR")
                    .build();

            Price price2 = aPrice()
                    .withPriceListId(2L)
                    .withDateRange(
                            LocalDateTime.of(2020, 6, 15, 14, 0, 0),
                            LocalDateTime.of(2020, 6, 15, 16, 0, 0))
                    .withMoney(BigDecimal.valueOf(20.00), "EUR")
                    .build();

            List<Price> prices = List.of(price1, price2);

            Optional<Price> result = Price.selectMostSpecific(prices);

            assertThat(result).isPresent();
            assertThat(result.get().getPriceListId().value()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("isApplicableAt")
    class IsApplicableAtTest {

        @Test
        @DisplayName("Should return true when date is within range")
        void shouldReturnTrueWhenDateIsWithinRange() {
            Price price = aPrice()
                    .withDateRange(
                            LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                            LocalDateTime.of(2020, 6, 14, 23, 59, 59))
                    .build();

            boolean result = price.isApplicableAt(LocalDateTime.of(2020, 6, 14, 12, 0, 0));

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should return true when date is at start of range")
        void shouldReturnTrueWhenDateIsAtStart() {
            Price price = aPrice()
                    .withDateRange(
                            LocalDateTime.of(2020, 6, 14, 10, 0, 0),
                            LocalDateTime.of(2020, 6, 14, 18, 0, 0))
                    .build();

            boolean result = price.isApplicableAt(LocalDateTime.of(2020, 6, 14, 10, 0, 0));

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should return true when date is at end of range")
        void shouldReturnTrueWhenDateIsAtEnd() {
            Price price = aPrice()
                    .withDateRange(
                            LocalDateTime.of(2020, 6, 14, 10, 0, 0),
                            LocalDateTime.of(2020, 6, 14, 18, 0, 0))
                    .build();

            boolean result = price.isApplicableAt(LocalDateTime.of(2020, 6, 14, 18, 0, 0));

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should return false when date is before range")
        void shouldReturnFalseWhenDateIsBefore() {
            Price price = aPrice()
                    .withDateRange(
                            LocalDateTime.of(2020, 6, 14, 10, 0, 0),
                            LocalDateTime.of(2020, 6, 14, 18, 0, 0))
                    .build();

            boolean result = price.isApplicableAt(LocalDateTime.of(2020, 6, 14, 9, 0, 0));

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should return false when date is after range")
        void shouldReturnFalseWhenDateIsAfter() {
            Price price = aPrice()
                    .withDateRange(
                            LocalDateTime.of(2020, 6, 14, 10, 0, 0),
                            LocalDateTime.of(2020, 6, 14, 18, 0, 0))
                    .build();

            boolean result = price.isApplicableAt(LocalDateTime.of(2020, 6, 14, 19, 0, 0));

            assertThat(result).isFalse();
        }
    }
}
