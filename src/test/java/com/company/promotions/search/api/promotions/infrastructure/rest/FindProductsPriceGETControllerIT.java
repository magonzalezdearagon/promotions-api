package com.company.promotions.search.api.promotions.infrastructure.rest;

import com.company.promotions.search.api.promotions.infrastructure.persistence.JpaPriceRepository;
import com.company.promotions.search.api.promotions.infrastructure.persistence.PriceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FindProductsPriceGETControllerIT {

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String VALID_API_KEY = "test-api-key-1";

    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("Test 1: Request at 10:00 on day 14 - should return base price")
    void shouldReturnBasePriceAt10OnDay14() throws Exception {
        mockMvc.perform(get("/prices")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    @DisplayName("Test 2: Request at 16:00 on day 14 - should return promotional price")
    void shouldReturnPromotionalPriceAt16OnDay14() throws Exception {
        mockMvc.perform(get("/prices")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45));
    }

    @Test
    @DisplayName("Test 3: Request at 21:00 on day 14 - should return base price")
    void shouldReturnBasePriceAt21OnDay14() throws Exception {
        mockMvc.perform(get("/prices")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("applicationDate", "2020-06-14T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    @DisplayName("Test 4: Request at 10:00 on day 15 - should return promotional price")
    void shouldReturnPromotionalPriceAt10OnDay15() throws Exception {
        mockMvc.perform(get("/prices")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("applicationDate", "2020-06-15T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(3))
                .andExpect(jsonPath("$.price").value(30.50));
    }

    @Test
    @DisplayName("Test 5: Request at 21:00 on day 16 - should return late promotional price")
    void shouldReturnLatePriceAt21OnDay16() throws Exception {
        mockMvc.perform(get("/prices")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("applicationDate", "2020-06-16T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(4))
                .andExpect(jsonPath("$.price").value(38.95));
    }

    @Test
    @DisplayName("Should return 404 when no price applies")
    void shouldReturn404WhenNoPriceApplies() throws Exception {
        mockMvc.perform(get("/prices")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("applicationDate", "2019-01-01T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when productId is invalid")
    void shouldReturn400WhenProductIdIsInvalid() throws Exception {
        mockMvc.perform(get("/prices")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "-1")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Product ID"));
    }

    @Test
    @DisplayName("Should return 400 when brandId is invalid")
    void shouldReturn400WhenBrandIdIsInvalid() throws Exception {
        mockMvc.perform(get("/prices")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Brand ID"));
    }

    @Test
    @DisplayName("Should return correct date range in response")
    void shouldReturnCorrectDateRangeInResponse() throws Exception {
        mockMvc.perform(get("/prices")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startDate").value("2020-06-14T15:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-06-14T18:30:00"));
    }

    @Test
    @DisplayName("Should return 401 when API key is missing")
    void shouldReturn401WhenApiKeyIsMissing() throws Exception {
        mockMvc.perform(get("/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 401 when API key is invalid")
    void shouldReturn401WhenApiKeyIsInvalid() throws Exception {
        mockMvc.perform(get("/prices")
                        .header(API_KEY_HEADER, "invalid-api-key")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should accept second API key for rotation support")
    void shouldAcceptSecondApiKeyForRotation() throws Exception {
        mockMvc.perform(get("/prices")
                        .header(API_KEY_HEADER, "test-api-key-2")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
