package com.invest.investment.services;

import com.invest.investment.repositories.mongo.UserRepository;
import com.invest.investment.repositories.postgres.AccountRepository;
import com.invest.investment.templates.AccountTemplate;
import com.invest.investment.templates.CreateAccountRequestTemplate;
import com.invest.investment.templates.UserTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void createAccount_whenRequestIsComplete_expectedReturnSuccess() {
        var request = CreateAccountRequestTemplate.build();
        var account = AccountTemplate.build();
        var user = UserTemplate.buildUserComplete();

        doReturn(user).when(userRepository).findByCpf(request.cpfOperator());
        doReturn(account).when(accountRepository).save(any());
        var output = accountService.createAccount(request);

        assertNotNull(output);
        assertEquals("Account test", request.description());
        assertEquals("12345678901", request.cpfOperator());
        assertEquals("12345678909876", request.contracteeDocumentNumber());

    }
}
