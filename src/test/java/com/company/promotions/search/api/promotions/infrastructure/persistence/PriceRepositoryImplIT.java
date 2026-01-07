package com.company.promotions.search.api.promotions.infrastructure.persistence;

import com.company.promotions.search.api.promotions.domain.Price;
import com.company.promotions.search.api.promotions.domain.PriceRepository;
import com.company.promotions.search.api.promotions.domain.vo.BrandId;
import com.company.promotions.search.api.promotions.domain.vo.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PriceRepositoryImplIT {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private JpaPriceRepository jpaPriceRepository;

    @BeforeEach
    void setUp() {
        jpaPriceRepository.deleteAll();
        insertTestData();
    }

    private void insertTestData() {
        // Price 1: Base price for product 35455, brand 1 (ZARA)
        PriceEntity price1 = new PriceEntity();
        price1.setBrandId(1L);
        price1.setStartDate(LocalDateTime.of(2020, 6, 14, 0, 0, 0));
        price1.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        price1.setPriceList(1);
        price1.setProductId(35455L);
        price1.setPriority(0);
        price1.setPrice(new BigDecimal("35.50"));
        price1.setCurrency("EUR");
        jpaPriceRepository.save(price1);

        // Price 2: Promotional price with higher priority
        PriceEntity price2 = new PriceEntity();
        price2.setBrandId(1L);
        price2.setStartDate(LocalDateTime.of(2020, 6, 14, 15, 0, 0));
        price2.setEndDate(LocalDateTime.of(2020, 6, 14, 18, 30, 0));
        price2.setPriceList(2);
        price2.setProductId(35455L);
        price2.setPriority(1);
        price2.setPrice(new BigDecimal("25.45"));
        price2.setCurrency("EUR");
        jpaPriceRepository.save(price2);

        // Price 3: Another promotional price
        PriceEntity price3 = new PriceEntity();
        price3.setBrandId(1L);
        price3.setStartDate(LocalDateTime.of(2020, 6, 15, 0, 0, 0));
        price3.setEndDate(LocalDateTime.of(2020, 6, 15, 11, 0, 0));
        price3.setPriceList(3);
        price3.setProductId(35455L);
        price3.setPriority(1);
        price3.setPrice(new BigDecimal("30.50"));
        price3.setCurrency("EUR");
        jpaPriceRepository.save(price3);

        // Price 4: Late promotional price
        PriceEntity price4 = new PriceEntity();
        price4.setBrandId(1L);
        price4.setStartDate(LocalDateTime.of(2020, 6, 15, 16, 0, 0));
        price4.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
        price4.setPriceList(4);
        price4.setProductId(35455L);
        price4.setPriority(1);
        price4.setPrice(new BigDecimal("38.95"));
        price4.setCurrency("EUR");
        jpaPriceRepository.save(price4);
    }

    @Test
    @DisplayName("Test 1: Request at 10:00 on day 14 - returns base price (only applicable)")
    void shouldReturnBasePriceAt10OnDay14() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

        List<Price> result = priceRepository.findApplicablePrices(
                ProductId.of(35455L), BrandId.of(1L), applicationDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPriceListId().value()).isEqualTo(1L);
        assertThat(result.get(0).getMoney().amount()).isEqualByComparingTo(new BigDecimal("35.50"));
    }

    @Test
    @DisplayName("Test 2: Request at 16:00 on day 14 - returns promotional price first (higher priority)")
    void shouldReturnPromotionalPriceFirstAt16OnDay14() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        List<Price> result = priceRepository.findApplicablePrices(
                ProductId.of(35455L), BrandId.of(1L), applicationDate);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPriceListId().value()).isEqualTo(2L);
        assertThat(result.get(0).getMoney().amount()).isEqualByComparingTo(new BigDecimal("25.45"));
    }

    @Test
    @DisplayName("Test 3: Request at 21:00 on day 14 - returns base price (only applicable)")
    void shouldReturnBasePriceAt21OnDay14() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 21, 0, 0);

        List<Price> result = priceRepository.findApplicablePrices(
                ProductId.of(35455L), BrandId.of(1L), applicationDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPriceListId().value()).isEqualTo(1L);
        assertThat(result.get(0).getMoney().amount()).isEqualByComparingTo(new BigDecimal("35.50"));
    }

    @Test
    @DisplayName("Test 4: Request at 10:00 on day 15 - returns promotional price first (higher priority)")
    void shouldReturnPromotionalPriceFirstAt10OnDay15() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0, 0);

        List<Price> result = priceRepository.findApplicablePrices(
                ProductId.of(35455L), BrandId.of(1L), applicationDate);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPriceListId().value()).isEqualTo(3L);
        assertThat(result.get(0).getMoney().amount()).isEqualByComparingTo(new BigDecimal("30.50"));
    }

    @Test
    @DisplayName("Test 5: Request at 21:00 on day 16 - returns late promotional price first (higher priority)")
    void shouldReturnLatePriceFirstAt21OnDay16() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 16, 21, 0, 0);

        List<Price> result = priceRepository.findApplicablePrices(
                ProductId.of(35455L), BrandId.of(1L), applicationDate);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPriceListId().value()).isEqualTo(4L);
        assertThat(result.get(0).getMoney().amount()).isEqualByComparingTo(new BigDecimal("38.95"));
    }

    @Test
    @DisplayName("Should return empty when no price applies")
    void shouldReturnEmptyWhenNoPriceApplies() {
        LocalDateTime applicationDate = LocalDateTime.of(2019, 1, 1, 10, 0, 0);

        List<Price> result = priceRepository.findApplicablePrices(
                ProductId.of(35455L), BrandId.of(1L), applicationDate);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty for non-existent product")
    void shouldReturnEmptyForNonExistentProduct() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

        List<Price> result = priceRepository.findApplicablePrices(
                ProductId.of(99999L), BrandId.of(1L), applicationDate);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return multiple applicable prices ordered by priority descending")
    void shouldReturnMultipleApplicablePricesOrderedByPriority() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        List<Price> result = priceRepository.findApplicablePrices(
                ProductId.of(35455L), BrandId.of(1L), applicationDate);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPriority().value()).isGreaterThan(result.get(1).getPriority().value());
        assertThat(result.get(0).getPriceListId().value()).isEqualTo(2L);
        assertThat(result.get(1).getPriceListId().value()).isEqualTo(1L);
    }
}
