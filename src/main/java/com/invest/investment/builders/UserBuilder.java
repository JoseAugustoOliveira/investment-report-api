package com.invest.investment.builders;

import com.invest.investment.entites.mongo.User;
import com.invest.investment.models.requests.CreateUserRequest;

import java.time.Instant;
import java.util.UUID;

public class UserBuilder {
    public static User builder(CreateUserRequest request, String password) {
        return User.builder()
                .id(UUID.randomUUID())
                .cpf(request.cpf())
                .name(request.name())
                .email(request.email())
                .password(password)
                .active(true)
                .creationTimeStamp(Instant.now())
                .updateTimestamp(null)
                .build();
    }
}
