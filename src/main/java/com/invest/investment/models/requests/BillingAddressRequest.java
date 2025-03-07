package com.invest.investment.models.requests;

import com.invest.investment.enums.State;
import com.invest.investment.utils.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record BillingAddressRequest(

        @NotBlank(message = "'name' must be informed")
        @Pattern(regexp = Regex.CPF, message = "'cpfOperator' must be a valid CPF")
        String cpfOperator,

        @NotBlank(message = "'street' must be informed")
        String street,

        @NotBlank(message = "'number' must be informed")
        String number,

        @NotBlank(message = "'neighbourhood' must be informed")
        String neighbourhood,

        @NotBlank(message = "'city' must be informed")
        String city,

        @NotBlank(message = "'state' must be informed")
        State state,

        @Schema(description = "Example 2024-01-20")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate billingDate) {}
