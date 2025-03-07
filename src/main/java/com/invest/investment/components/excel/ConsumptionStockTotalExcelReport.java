package com.invest.investment.components.excel;

import com.invest.investment.models.responses.StockReportTotal;
import org.dhatim.fastexcel.Worksheet;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.invest.investment.utils.NumberUtil.currencyFormat;

public class ConsumptionStockTotalExcelReport extends StockExcelReportGenerator<StockReportTotal> {

    private static final String EMPTY_VALUES = "N/A";

    private final String[] headers = {
            "AÇÃO/FIIS", "Quantidade", "Valor Total", "Preço Médio",
            "Preço atualizado do dia", "Vr Range em 52 semanas"};
    @Override
    protected String[] getHeaders() {
        return headers;
    }

    @Override
    protected Consumer<Worksheet> addContent(List<StockReportTotal> items) {
        return (Worksheet ws) -> {
            var rowIndex = new AtomicInteger(1);
            items.forEach(item -> {
               var columnIndex = new AtomicInteger(0);
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), item.stock());
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), item.quantity());
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), currencyFormat.format(item.totalValue()));
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), currencyFormat.format(item.averagePrice()));
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), currencyFormat.format(item.valueOfDay()));
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), item.fiftyTwoWeekRange());

                rowIndex.incrementAndGet();
            });
        };
    }
}
