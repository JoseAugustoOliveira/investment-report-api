package com.invest.investment.models.responses;

import com.invest.investment.entites.postgres.Stock;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record AccountInfoResponse(
        String name,
        Instant creationDate,
        List<Stock> stocks) {}
