package com.invest.investment.models.requests;

import com.invest.investment.utils.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateAccountRequest(
        String description,

        // TODO: validation for audit, yet in developing
        @NotBlank(message = "'cpfOperator' must be informed")
        @Pattern(regexp = Regex.CPF, message = "'cpfOperator' must be a valid CPF")
        String cpfOperator,

        @Pattern(regexp = Regex.CNPJ, message = "'contracteeDocumentNumber' must be a valid CNPJ")
        String contracteeDocumentNumber) {}
