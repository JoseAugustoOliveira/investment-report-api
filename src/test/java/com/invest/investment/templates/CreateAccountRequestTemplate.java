package com.invest.investment.templates;

import com.invest.investment.models.requests.CreateAccountRequest;

public class CreateAccountRequestTemplate {
    public static CreateAccountRequest build() {
        return CreateAccountRequest.builder()
                .description("Account test")
                .cpfOperator("12345678901")
                .contracteeDocumentNumber("12345678909876")
                .build();
    }
}
