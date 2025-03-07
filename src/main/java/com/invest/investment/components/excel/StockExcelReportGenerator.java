package com.invest.investment.components.excel;

import com.invest.investment.components.ReportGenerator;
import com.invest.investment.utils.DateUtil;
import org.dhatim.fastexcel.Worksheet;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

abstract class StockExcelReportGenerator<T> implements ReportGenerator<T> {

    protected ExcelReportWriter excelReportWriter = new ExcelReportWriter();
    protected abstract String[] getHeaders();
    protected abstract Consumer<Worksheet> addContent(List<T> items);

    @Override
    public File generate(List<T> items) throws IOException {
        String fileName = "Relatorio_" + LocalDate.now().format(DateUtil.isoDateFormatReport);

        var metadata = ExcelReportMetadata.builder()
                .fileName(fileName + ".xlsx")
                .sheetName("Acoes e Fiis")
                .applicationName("Financial")
                .applicationVersion("1.0")
                .headers(getHeaders())
                .build();

        return excelReportWriter.createExcel(metadata, addContent(items));
    }
}
