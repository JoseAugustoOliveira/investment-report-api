package com.invest.investment.components.excel;

import com.invest.investment.models.responses.StockReportByYear;
import org.dhatim.fastexcel.Worksheet;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.invest.investment.utils.DateUtil.isoDateFormat;
import static com.invest.investment.utils.NumberUtil.currencyFormat;

public class ConsumptionStockByYearExcelReport extends StockExcelReportGenerator<StockReportByYear> {

    private static final String EMPTY_VALUES = "N/A";

    private final String[] headers = {
            "AÇÃO/FIIS", "Quantidade", "Preço de Compra", "Preço atualizado do dia", "Preço Total da Compra",
            "Data de Compra", "Vr Range em 52 semanas", "Preço de Venda", "Data de Venda", "Lucro/Prejuizo"
    };

    @Override
    protected String[] getHeaders() {
        return headers;
    }

    @Override
    protected Consumer<Worksheet> addContent(List<StockReportByYear> items) {
        return (Worksheet ws) -> {
            var rowIndex = new AtomicInteger(1);
            items.forEach(item -> {
                var columnIndex = new AtomicInteger(0);
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), item.stock());
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), item.quantity());
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), currencyFormat.format(item.buyPrice()));
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), currencyFormat.format(item.valueOfDay()));
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), currencyFormat.format(item.totalBuy()));
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), item.buyDate().format(isoDateFormat));

                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), item.fiftyTwoWeekRange());
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), item.salePrice());
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(), item.saleDate() != null ? item.saleDate().format(isoDateFormat) : EMPTY_VALUES);
                ws.value(rowIndex.get(), columnIndex.getAndIncrement(),  currencyFormat.format(item.gain()));

                rowIndex.incrementAndGet();
            });
        };
    }

}
