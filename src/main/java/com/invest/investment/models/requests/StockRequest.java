package com.invest.investment.models.requests;

import com.invest.investment.utils.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record StockRequest(

        @NotBlank(message = "'cpf' must be informed")
        @Pattern(regexp = Regex.CPF, message = "'cpf' must be a valid CPF")
        String cpf,

        @NotBlank(message = "'stock' must be informed")
        String stock,

        @NotNull(message = "'quantity' must be informed")
        @PositiveOrZero(message = "Page must have only numbers between 0 -> 999")
        Long quantity,

        String description,

        @NotNull(message = "'buyPrice' must be informed")
        BigDecimal buyPrice) {}
