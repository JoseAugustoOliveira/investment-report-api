package com.invest.investment.models.responses;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record StockReportByYear(
        String stock,
        Long quantity,
        BigDecimal buyPrice,
        BigDecimal valueOfDay,
        String fiftyTwoWeekRange,
        BigDecimal totalBuy,
        BigDecimal salePrice,
        BigDecimal gain,
        LocalDate buyDate,
        LocalDate saleDate) {}
