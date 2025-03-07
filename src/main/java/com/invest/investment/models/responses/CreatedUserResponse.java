package com.invest.investment.models.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreatedUserResponse(
        String id,
        String name,
        String email) {}