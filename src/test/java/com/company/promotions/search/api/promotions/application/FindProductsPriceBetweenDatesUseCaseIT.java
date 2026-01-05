package com.company.promotions.search.api.promotions.application;

import com.company.promotions.search.api.promotions.infrastructure.persistence.JpaPriceRepository;
import com.company.promotions.search.api.promotions.infrastructure.persistence.PriceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FindProductsPriceBetweenDatesUseCaseIT {

    @Autowired
    private FindProductsPriceBetweenDatesUseCase useCase;

    @Autowired
    private JpaPriceRepository jpaPriceRepository;

    @BeforeEach
    void setUp() {
        jpaPriceRepository.deleteAll();
        insertTestData();
    }

    private void insertTestData() {
        // Price 1: Base price for product 35455, brand 1 (ZARA) - wide range
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

        // Price 2: Promotional price - narrow range (3.5 hours)
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

        // Price 3: Another promotional price - medium range (11 hours)
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

        // Price 4: Late promotional price - wide range
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
    @DisplayName("Test 1: Request at 10:00 on day 14 - should return base price (only one applies)")
    void shouldReturnBasePriceAt10OnDay14() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 1L);

        assertThat(result).isPresent();
        assertThat(result.get().priceList()).isEqualTo(1);
        assertThat(result.get().price()).isEqualByComparingTo(new BigDecimal("35.50"));
    }

    @Test
    @DisplayName("Test 2: Request at 16:00 on day 14 - should return promotional price (smallest range)")
    void shouldReturnPromotionalPriceAt16OnDay14() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 1L);

        assertThat(result).isPresent();
        // Price 2 has a 3.5-hour range vs Price 1's 6-month range
        assertThat(result.get().priceList()).isEqualTo(2);
        assertThat(result.get().price()).isEqualByComparingTo(new BigDecimal("25.45"));
    }

    @Test
    @DisplayName("Test 3: Request at 21:00 on day 14 - should return base price (only one applies)")
    void shouldReturnBasePriceAt21OnDay14() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 21, 0, 0);

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 1L);

        assertThat(result).isPresent();
        assertThat(result.get().priceList()).isEqualTo(1);
        assertThat(result.get().price()).isEqualByComparingTo(new BigDecimal("35.50"));
    }

    @Test
    @DisplayName("Test 4: Request at 10:00 on day 15 - should return promotional price (smallest range)")
    void shouldReturnPromotionalPriceAt10OnDay15() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0, 0);

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 1L);

        assertThat(result).isPresent();
        // Price 3 has an 11-hour range vs Price 1's 6-month range
        assertThat(result.get().priceList()).isEqualTo(3);
        assertThat(result.get().price()).isEqualByComparingTo(new BigDecimal("30.50"));
    }

    @Test
    @DisplayName("Test 5: Request at 21:00 on day 16 - should return late promotional price")
    void shouldReturnLatePriceAt21OnDay16() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 16, 21, 0, 0);

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 1L);

        assertThat(result).isPresent();
        // Both Price 1 and Price 4 apply with similar ranges, but Price 4 starts later
        // so it has a slightly smaller range
        assertThat(result.get().priceList()).isEqualTo(4);
        assertThat(result.get().price()).isEqualByComparingTo(new BigDecimal("38.95"));
    }

    @Test
    @DisplayName("Should return empty when no price applies")
    void shouldReturnEmptyWhenNoPriceApplies() {
        LocalDateTime applicationDate = LocalDateTime.of(2019, 1, 1, 10, 0, 0);

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 1L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty for non-existent product")
    void shouldReturnEmptyForNonExistentProduct() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 99999L, 1L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty for non-existent brand")
    void shouldReturnEmptyForNonExistentBrand() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 99L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return correct date range in response")
    void shouldReturnCorrectDateRangeInResponse() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 1L);

        assertThat(result).isPresent();
        assertThat(result.get().startDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 15, 0, 0));
        assertThat(result.get().endDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 18, 30, 0));
    }
}
