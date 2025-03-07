package com.invest.investment.controllers.v1;

import com.invest.investment.models.requests.BillingAddressRequest;
import com.invest.investment.models.requests.CreateAccountRequest;
import com.invest.investment.models.responses.AccountInfoResponse;
import com.invest.investment.models.responses.UserWithAccountResponse;
import com.invest.investment.services.AccountService;
import com.invest.investment.services.BillingAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Account")
@RequestMapping("/v1/account")
public class AccountController {

    private final AccountService accountService;
    private final BillingAddressService billingAddressService;

    @PostMapping
    @Operation(summary = "Register new Account with User registered")
    public UserWithAccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @PostMapping("/billing-address")
    @Operation(summary = "Update address billing of User Account")
    public ResponseEntity<String> createBillingAddress(@Valid @RequestBody BillingAddressRequest request) {
        return billingAddressService.createBillingAddress(request);
    }

    @GetMapping("/{cpf}")
    @Operation(summary = "List Account and Stock infos")
    public AccountInfoResponse listAccountInfo(@PathVariable String cpf) {
        return accountService.listAccountInfo(cpf);
    }
}
