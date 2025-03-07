package com.invest.investment.components;

import com.invest.investment.clients.BrapiClient;
import com.invest.investment.models.StockDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockQuotesComponent {

    private final BrapiClient brapiClient;

    public Map<String, StockDto> getStockDtoMap(Set<String> stockIds, String token) {
        return stockIds.stream().collect(Collectors.toMap(
                stockId -> stockId,
                stockId -> {
                    try {
                        return brapiClient.getQuote(token, stockId).results().getFirst();
                    } catch (Exception e) {
                        log.warn("Can't possible take this stock: {}", stockId, e);
                        return null;
                    }
                }
        ));
    }
}
