package com.invest.investment.models.responses;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record StockReportTotal(
        String stock,
        Long quantity,
        BigDecimal totalValue,
        BigDecimal averagePrice,
        BigDecimal valueOfDay,
        String fiftyTwoWeekRange) {}
