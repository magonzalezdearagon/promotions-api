CREATE TABLE promotions.prices
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id   BIGINT         NOT NULL,
    start_date TIMESTAMP      NOT NULL,
    end_date   TIMESTAMP      NOT NULL,
    price_list INT            NOT NULL,
    product_id BIGINT         NOT NULL,
    priority   INT            NOT NULL DEFAULT 0,
    price      DECIMAL(10, 2) NOT NULL,
    currency   VARCHAR(3)     NOT NULL
);

CREATE INDEX idx_brand_product_prices_dates ON promotions.prices (product_id, brand_id, start_date, end_date);