package com.invest.investment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.NOT_FOUND
)
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String msg) {
        super();
    }

    public EntityNotFoundException(String msg, String document) {
        super();
    }
}
