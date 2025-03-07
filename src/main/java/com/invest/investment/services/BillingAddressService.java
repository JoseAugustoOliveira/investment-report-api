package com.invest.investment.services;

import com.invest.investment.entites.postgres.Account;
import com.invest.investment.entites.postgres.BillingAddress;
import com.invest.investment.exceptions.EntityNotFoundException;
import com.invest.investment.models.requests.BillingAddressRequest;
import com.invest.investment.repositories.postgres.AccountRepository;
import com.invest.investment.repositories.postgres.BillingAddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingAddressService {

    private final AccountRepository accountRepository;
    private final BillingAddressRepository billingAddressRepository;

    public ResponseEntity<String> createBillingAddress(BillingAddressRequest request) {
        Account account = accountRepository.findByCpf(request.cpfOperator())
                .orElseThrow(() -> new EntityNotFoundException("In process to create billing address, account not found for CPF: " + request.cpfOperator()));

        BillingAddress newBillingAddress = BillingAddress.builder()
                .billingDate(request.billingDate())
                .state(request.state())
                .city(request.city())
                .number(request.number())
                .neighbourhood(request.neighbourhood())
                .street(request.street())
                .billingDate(request.billingDate())
                .account(account)
                .build();

        billingAddressRepository.save(newBillingAddress);
        return ResponseEntity.ok("Billing address created successfully");
    }
}
