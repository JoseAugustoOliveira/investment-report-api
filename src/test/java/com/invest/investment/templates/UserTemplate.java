package com.invest.investment.templates;

import com.invest.investment.entites.mongo.User;

import java.time.Instant;
import java.util.UUID;

public class UserTemplate {
    public static User buildUserComplete() {
        return User.builder()
                .name("guto test")
                .email("test@email.com")
                .id(UUID.randomUUID())
                .active(true)
                .cpf("12345678764")
                .password("hashedPassword")
                .updateTimestamp(null)
                .creationTimeStamp(Instant.now())
                .build();
    }
}
