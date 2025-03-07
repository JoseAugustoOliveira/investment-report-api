package com.invest.investment.components;

import com.invest.investment.repositories.mongo.UserRepository;
import com.invest.investment.templates.UserTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ActiveOrInactiveUserComponentTest {

    @InjectMocks
    private ActiveOrInactiveUserComponent activeOrInactiveUserComponent;

    @Mock
    private UserRepository userRepository;

    @Test
    void changeUserStatus_whenUserIsActive_expectedReturnSuccessMessage() {
        var cpf = "12345678901";
        var user = UserTemplate.buildUserComplete();
        user.setCpf(cpf);
        user.setActive(true);

        doReturn(Optional.of(user)).when(userRepository).findByCpf(cpf);
        var output = activeOrInactiveUserComponent.changeUserStatus(
                cpf,
                true,
                "User with CPF {cpf} is already active.",
                "User with CPF {cpf} has been successfully reactivated."
        );

        assertNotNull(output);
        assertEquals("User with CPF " + cpf + " is already active.", output);
    }

    @Test
    void changeUserStatus_whenUserIsInactive_expectedReturnSuccessMessage() {
        var cpf = "12345678901";
        var user = UserTemplate.buildUserComplete();
        user.setCpf(cpf);
        user.setActive(false);

        doReturn(Optional.of(user)).when(userRepository).findByCpf(cpf);
        var output = activeOrInactiveUserComponent.changeUserStatus(
                cpf,
                true,
                "User with CPF {cpf} is already active.",
                "User with CPF {cpf} has been successfully reactivated."
        );

        assertNotNull(output);
        assertEquals("User with CPF " + cpf + " has been successfully reactivated.", output);
    }
}
