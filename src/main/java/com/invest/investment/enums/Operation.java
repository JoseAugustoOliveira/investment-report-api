package com.invest.investment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Operation {
    DOWNLOAD_SPREADSHEET(1, "Download Relatório"),
    REGISTER_ACCOUNT(2, "Adicionando Conta"),
    REGISTER_STOCKS(3, "Registro de Ações ou Fiis"),
    SALE_STOCKS(4, "Venda de Ações ou Fiis");

    private final int value;
    private final String operationName;

    public static Operation fromOperationName(String name) {
        return Arrays.stream(Operation.values())
                .filter(op -> op.getOperationName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid operation: " + name));
    }
}
