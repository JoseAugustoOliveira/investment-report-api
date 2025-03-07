package com.invest.investment.builders;

import com.invest.investment.entites.mongo.User;
import com.invest.investment.models.responses.CreatedUserResponse;

public class CreatedUserResponseBuilder {

    public static CreatedUserResponse build(User newUser) {
        return CreatedUserResponse.builder()
                .id(String.valueOf(newUser.getId()))
                .email(newUser.getEmail())
                .name(newUser.getName())
                .build();
    }
}
