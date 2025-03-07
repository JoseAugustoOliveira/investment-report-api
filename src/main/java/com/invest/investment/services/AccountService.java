package com.invest.investment.services;

import com.invest.investment.components.AuditComponent;
import com.invest.investment.entites.postgres.Account;
import com.invest.investment.enums.Operation;
import com.invest.investment.exceptions.AccountExistsException;
import com.invest.investment.exceptions.EntityNotFoundException;
import com.invest.investment.models.requests.CreateAccountRequest;
import com.invest.investment.models.responses.AccountInfoResponse;
import com.invest.investment.models.responses.UserWithAccountResponse;
import com.invest.investment.repositories.mongo.UserRepository;
import com.invest.investment.repositories.postgres.AccountRepository;
import com.invest.investment.repositories.postgres.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AuditComponent auditComponent;

    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final AccountRepository accountRepository;

    private static final String NOT_USED = "Documento desnecessário";

    public UserWithAccountResponse createAccount(CreateAccountRequest accountRequest) {
        var instantTime = Instant.now();
        var userName = userRepository.findByCpf(accountRequest.cpfOperator())
                .orElseThrow(() -> new EntityNotFoundException("User not found for this cpf: {}", accountRequest.cpfOperator()));
        var contracteeDocumentNumber = ObjectUtils.isEmpty(accountRequest.contracteeDocumentNumber()) ? accountRequest.contracteeDocumentNumber() : NOT_USED;

        var existingAccount = accountRepository.findByCpfOrContracteeDocumentNumber(
                accountRequest.cpfOperator(),
                accountRequest.contracteeDocumentNumber());

        if (existingAccount.isPresent()) {
            throw new AccountExistsException("Account exists for this document: " + accountRequest.cpfOperator(), accountRequest.contracteeDocumentNumber());
        }

        var newAccount = Account.builder()
                .name(userName.getName())
                .contracteeDocumentNumber(contracteeDocumentNumber)
                .cpf(accountRequest.cpfOperator())
                .description(accountRequest.description())
                .creationTimeStamp(instantTime)
                .stocks(List.of())
                .build();

        accountRepository.save(newAccount);
        auditComponent.saveAudit(accountRequest.cpfOperator(), Operation.REGISTER_ACCOUNT);

    return UserWithAccountResponse.builder()
            .name(userName.getName())
            .contracteeDocumentNumber(contracteeDocumentNumber)
            .cpf(accountRequest.cpfOperator())
            .creationTimeStamp(instantTime)
            .build();
    }

    public AccountInfoResponse listAccountInfo(String cpf) {
        var stocks = stockRepository.findByAccount_Cpf(cpf);

        if (stocks.isEmpty()) {
            log.error("Stocks not found for this CPF: {}", cpf);
        }

        var account = accountRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for this cpf: ", cpf));

        return AccountInfoResponse.builder()
                .creationDate(account.getCreationTimeStamp())
                .name(account.getName())
                .stocks(stocks.stream().toList())
                .build();
    }
}
