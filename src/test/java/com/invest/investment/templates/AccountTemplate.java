package com.invest.investment.templates;

import com.invest.investment.entites.postgres.Account;

import java.time.Instant;

public class AccountTemplate {
    public static Account build() {
        return Account.builder()
                .description("Account test")
                .name("Account test")
                .contracteeDocumentNumber("12345678909876")
                .cpf("12345678901")
                .creationTimeStamp(Instant.now())
                .build();
    }
}
