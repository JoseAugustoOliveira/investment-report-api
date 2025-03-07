package com.invest.investment.clients;

import com.invest.investment.configs.FeignConfig;
import com.invest.investment.models.BrapiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "BrapiClient",
        url = "${api.brapi}",
        configuration = FeignConfig.class)
public interface BrapiClient {

    @GetMapping(value = "/quote/{stockId}")
    BrapiResponse getQuote(@RequestParam("token") String token, @PathVariable("stockId") String stockId);
}
