package com.invest.investment.models.requests;

import com.invest.investment.utils.Regex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateUserRequest(
        @NotBlank(message = "'name' must be informed")
        String name,

        @NotBlank(message = "'cpf' must be informed")
        @Pattern(regexp = Regex.CPF, message = "'cpf' must be a valid CPF")
        String cpf,

        @Email(message = "'email' must be informed")
        String email,

        @NotBlank(message = "'password' must be informed")
        String password) {}