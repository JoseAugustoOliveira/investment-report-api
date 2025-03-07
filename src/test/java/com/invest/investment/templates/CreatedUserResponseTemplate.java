package com.invest.investment.templates;

import com.invest.investment.models.responses.CreatedUserResponse;

public class CreatedUserResponseTemplate {
    public static CreatedUserResponse build() {
        return CreatedUserResponse.builder()
                .name("guto test")
                .email("test@email.com")
                .id("1")
                .build();
    }
}
