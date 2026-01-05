package com.company.promotions.search.api.promotions.application;

import com.company.promotions.search.api.promotions.domain.Price;
import com.company.promotions.search.api.promotions.domain.PriceRepository;
import com.company.promotions.search.api.promotions.domain.vo.BrandId;
import com.company.promotions.search.api.promotions.domain.vo.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.company.promotions.search.api.promotions.domain.PriceBuilder.aPrice;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindProductsPriceBetweenDatesUseCaseTest {

    @Mock
    private PriceRepository priceRepository;

    private FindProductsPriceBetweenDatesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindProductsPriceBetweenDatesUseCase(priceRepository);
    }

    @Test
    @DisplayName("Should return empty when no prices found")
    void shouldReturnEmptyWhenNoPricesFound() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        when(priceRepository.findApplicablePrices(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 1L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return the only applicable price")
    void shouldReturnOnlyApplicablePrice() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        Price price = aPrice()
                .withProductId(35455L)
                .withBrandId(1L)
                .withPriceListId(1L)
                .withDateRange(
                        LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                        LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .withMoney(BigDecimal.valueOf(35.50), "EUR")
                .build();

        when(priceRepository.findApplicablePrices(
                eq(ProductId.of(35455L)),
                eq(BrandId.of(1L)),
                eq(applicationDate)))
                .thenReturn(List.of(price));

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 1L);

        assertThat(result).isPresent();
        assertThat(result.get().productId()).isEqualTo(35455L);
        assertThat(result.get().brandId()).isEqualTo(1L);
        assertThat(result.get().priceList()).isEqualTo(1);
        assertThat(result.get().price()).isEqualByComparingTo(BigDecimal.valueOf(35.50));
    }

    @Test
    @DisplayName("Should return price with smallest date range when multiple prices apply")
    void shouldReturnPriceWithSmallestDateRangeWhenMultiplePricesApply() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        // Wide range price (base price)
        Price wideRangePrice = aPrice()
                .withProductId(35455L)
                .withBrandId(1L)
                .withPriceListId(1L)
                .withDateRange(
                        LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                        LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .withMoney(BigDecimal.valueOf(35.50), "EUR")
                .build();

        // Narrow range price (promotional price)
        Price narrowRangePrice = aPrice()
                .withProductId(35455L)
                .withBrandId(1L)
                .withPriceListId(2L)
                .withDateRange(
                        LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                        LocalDateTime.of(2020, 6, 14, 18, 30, 0))
                .withMoney(BigDecimal.valueOf(25.45), "EUR")
                .build();

        when(priceRepository.findApplicablePrices(any(), any(), any()))
                .thenReturn(List.of(wideRangePrice, narrowRangePrice));

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 1L);

        assertThat(result).isPresent();
        assertThat(result.get().priceList()).isEqualTo(2);
        assertThat(result.get().price()).isEqualByComparingTo(BigDecimal.valueOf(25.45));
    }

    @Test
    @DisplayName("Should call repository with correct parameters")
    void shouldCallRepositoryWithCorrectParameters() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        when(priceRepository.findApplicablePrices(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        useCase.execute(applicationDate, 35455L, 1L);

        verify(priceRepository).findApplicablePrices(
                ProductId.of(35455L),
                BrandId.of(1L),
                applicationDate);
    }

    @Test
    @DisplayName("Should map all fields correctly to DTO")
    void shouldMapAllFieldsCorrectlyToDto() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59);

        Price price = aPrice()
                .withProductId(35455L)
                .withBrandId(1L)
                .withPriceListId(3L)
                .withDateRange(startDate, endDate)
                .withMoney(BigDecimal.valueOf(30.50), "EUR")
                .build();

        when(priceRepository.findApplicablePrices(any(), any(), any()))
                .thenReturn(List.of(price));

        Optional<ApplicablePriceDto> result = useCase.execute(applicationDate, 35455L, 1L);

        assertThat(result).isPresent();
        ApplicablePriceDto dto = result.get();
        assertThat(dto.productId()).isEqualTo(35455L);
        assertThat(dto.brandId()).isEqualTo(1L);
        assertThat(dto.priceList()).isEqualTo(3);
        assertThat(dto.startDate()).isEqualTo(startDate);
        assertThat(dto.endDate()).isEqualTo(endDate);
        assertThat(dto.price()).isEqualByComparingTo(BigDecimal.valueOf(30.50));
    }
}
