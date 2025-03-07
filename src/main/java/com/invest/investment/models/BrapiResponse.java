package com.invest.investment.models;

import lombok.Builder;

import java.util.List;

@Builder
public record BrapiResponse(List<StockDto> results) {
}
