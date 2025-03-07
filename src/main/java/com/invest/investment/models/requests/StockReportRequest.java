package com.invest.investment.models.requests;

import com.invest.investment.enums.ReportType;
import com.invest.investment.utils.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record StockReportRequest(

        @NotBlank(message = "'cpf' must be informed")
        @Pattern(regexp = Regex.CPF, message = "'cpf' must be a valid CPF")
        String cpf,

        @NotNull(message = "Report type must be 'BY_YEAR' or 'TOTAL'.")
        ReportType type,

        @Pattern(regexp = Regex.YEAR, message = "'year' must be a valid data, ex: 2025")
        Integer year) {}
