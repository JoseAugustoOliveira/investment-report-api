package com.invest.investment.configs;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static feign.FeignException.errorStatus;

public class FeignErrorDecoder implements ErrorDecoder {

    @SneakyThrows(IOException.class)
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, new String(response.body().asInputStream().readAllBytes()));
        } else if (response.status() == 409) {
            return new ResponseStatusException(HttpStatus.CONFLICT, new String(response.body().asInputStream().readAllBytes()));
        } else if (response.status() == 400) {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, getErrorMessage(response));
        } else if (response.status() == 401) {
            return new ResponseStatusException(HttpStatus.UNAUTHORIZED, getErrorMessage(response));
        } else if (response.status() == 500 || response.status() == 503) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, new String(response.body().asInputStream().readAllBytes()));
        }

        return errorStatus(methodKey, response);
    }

    private static String getErrorMessage(Response response) throws IOException {
        String errorMessage = "";
        if (response.status() == 401) {
            errorMessage = "Error 401 - Unauthorized";
        }
        if (response.body() != null) {
            try (Reader reader = response.body().asReader(StandardCharsets.UTF_8)){
                errorMessage = IOUtils.toString(reader);
            }
        }
        return errorMessage;
    }
}
