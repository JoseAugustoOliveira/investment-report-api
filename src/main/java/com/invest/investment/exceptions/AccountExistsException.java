package com.invest.investment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.INTERNAL_SERVER_ERROR
)
public class AccountExistsException extends RuntimeException {
    public AccountExistsException(String msg, String document) {
        super();
    }
}
