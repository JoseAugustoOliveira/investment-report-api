package com.invest.investment.components.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExcelReportMetadata {
    private String fileName;
    private String sheetName;
    private String applicationName;
    private String applicationVersion;
    private String[] headers;
}
