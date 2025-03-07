package com.invest.investment.models;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record StockDto(
        BigDecimal regularMarketPrice,
        String fiftyTwoWeekRange) {}
