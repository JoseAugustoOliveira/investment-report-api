package com.invest.investment.components;

import com.invest.investment.clients.BrapiClient;
import com.invest.investment.entites.postgres.Stock;
import com.invest.investment.models.StockDto;
import com.invest.investment.models.responses.StockReportTotal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CalculateStocksComponent {

    private final BrapiClient brapiClient;

    public BigDecimal calculateQuantityAndBuyPrice(BigDecimal buyPrice, Integer quantity) {
        return buyPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getGainPerBuyEachStock(BigDecimal buyPrice, BigDecimal marketPrice, Long quantity) {
        var eachStockValue = marketPrice.subtract(buyPrice);
        return eachStockValue.multiply(BigDecimal.valueOf(quantity));
    }

    public List<StockReportTotal> getStockTotals(List<Stock> stocks, String token) {
        Set<String> stockIds = stocks.stream()
                .map(Stock::getStockId)
                .collect(Collectors.toSet());

        Map<String, StockDto> stockQuotes = stockIds.stream()
                .collect(Collectors.toMap(
                        stockId -> stockId,
                        stockId -> brapiClient.getQuote(token, stockId).results().getFirst()
                ));

        return stocks.stream()
                .collect(Collectors.groupingBy(Stock::getStockId))
                .entrySet().stream()
                .map(entry -> {
                    String stockId = entry.getKey();
                    List<Stock> stockList = entry.getValue();

                    long totalQuantity = getTotalQuantityForStockId(stockList);
                    BigDecimal totalValue = getTotalValue(stockList);
                    BigDecimal averagePrice = getAveragePrice(totalQuantity, totalValue);

                    StockDto stockData = stockQuotes.get(stockId);
                    BigDecimal valueOfDay = stockData.regularMarketPrice();
                    String range = stockData.fiftyTwoWeekRange();

                    return new StockReportTotal(stockId, totalQuantity, totalValue, averagePrice, valueOfDay, range);
                }).toList();
    }


    private long getTotalQuantityForStockId(List<Stock> stockList) {
        return stockList.stream()
                .mapToLong(Stock::getQuantity)
                .sum();
    }

    public BigDecimal getTotalValue(List<Stock> stockList) {
        return stockList.stream()
                .map(stock -> stock.getBuyPrice()
                .multiply(BigDecimal.valueOf(stock.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getPageTotalValue(Page<Stock> stockList) {
        return stockList.stream()
                .map(stock -> stock.getBuyPrice()
                        .multiply(BigDecimal.valueOf(stock.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getAveragePrice(long totalQuantity, BigDecimal totalValue) {
        return totalQuantity > 0
                ? totalValue.divide(BigDecimal.valueOf(totalQuantity), RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
    }
}
