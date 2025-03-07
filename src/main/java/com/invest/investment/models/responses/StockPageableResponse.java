package com.invest.investment.models.responses;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StockPageableResponse(
        String stock,
        Long quantity,
        BigDecimal buyPrice,
        BigDecimal valueOfDay,
        String fiftyTwoWeekRange,
        BigDecimal totalBuy,
        BigDecimal salePrice,
        BigDecimal gain,
        LocalDate buyDate,
        LocalDate saleDate,
        BigDecimal totalValue,
        BigDecimal averagePrice) {}
