package com.invest.investment.models.responses;

import lombok.Builder;

import java.time.Instant;

@Builder
public record UserWithAccountResponse(
        String name,
        Instant creationTimeStamp,
        String cpf,
        String contracteeDocumentNumber) {}
