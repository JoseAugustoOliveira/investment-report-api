package com.invest.investment.components.excel;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ExcelReportWriter {
    public File createExcel(ExcelReportMetadata metadata, Consumer<Worksheet> addRows) throws IOException {
        var path = Paths.get(System.getProperty("user.home"), metadata.getFileName());
        try (OutputStream os = Files.newOutputStream(path)) {
            Workbook wb = new Workbook(os, metadata.getApplicationName(), metadata.getApplicationVersion());
            Worksheet ws = wb.newWorksheet(metadata.getSheetName());
            addHeader(ws, metadata.getHeaders());
            addRows.accept(ws);
            ws.close();
            wb.close();
        }
        return path.toFile();
    }

    private void addHeader(Worksheet ws, String[] headers) {
        var columnIndex = new AtomicInteger(0);
        for (String header : headers) {
            ws.value(0, columnIndex.getAndIncrement(), header);
        }
    }
}
