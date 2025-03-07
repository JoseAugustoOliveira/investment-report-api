package com.invest.investment.templates;

import com.invest.investment.models.requests.CreateUserRequest;

public class CreateUserRequestTemplate {
    public static CreateUserRequest buildRequestComplete() {
        return CreateUserRequest.builder()
                .cpf("12345678764")
                .email("test@email.com")
                .password("1234")
                .name("guto test")
                .build();
    }

    public static CreateUserRequest buildRequestWithoutCpf() {
        return CreateUserRequest.builder()
                .email("test@email.com")
                .password("1234")
                .name("guto test")
                .build();
    }
}
