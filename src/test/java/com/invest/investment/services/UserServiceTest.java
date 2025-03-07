package com.invest.investment.services;

import com.invest.investment.components.ActiveOrInactiveUserComponent;
import com.invest.investment.exceptions.EntityErrorException;
import com.invest.investment.exceptions.EntityNotFoundException;
import com.invest.investment.repositories.mongo.UserRepository;
import com.invest.investment.templates.CreateUserRequestTemplate;
import com.invest.investment.templates.UserTemplate;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private ActiveOrInactiveUserComponent activeOrInactiveUserComponent;

    @Before("")
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_whenRequestIsComplete_expectedReturnSuccess() {
       var request = CreateUserRequestTemplate.buildRequestComplete();
       var user = UserTemplate.buildUserComplete();

       doReturn(user).when(userRepository).save(any());
       doReturn("hasherPassword").when(passwordEncoder).encode(any());

       var output = userService.createUser(request);

       assertNotNull(output);
       assertEquals("test@email.com", request.email());
       assertEquals("guto test", request.name());
       assertEquals("12345678764", request.cpf());
    }

    @Test
    void getUserByCpf_whenCpfIsValid_expectedReturnSuccess() {
        var cpf = "12345678901";
        var user = UserTemplate.buildUserComplete();
        user.setCpf(cpf);

        doReturn(Optional.of(user)).when(userRepository).findByCpf(any());
        var output = userService.getUserByCpf(cpf);

        assertNotNull(output);
        assertEquals(user.getName(), output.name());
        assertEquals(user.getEmail(), output.email());
    }

    @Test
    void getUserByCpf_whenCpfIsInvalid_expectedReturnException() {
        var cpf = "12345678901";
        doReturn(Optional.empty()).when(userRepository).findByCpf(any());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserByCpf(cpf);
        });
    }

    @Test
    void listAllUsers_whenUserExists_expectedReturnUserList() {
        var users = List.of(UserTemplate.buildUserComplete(), UserTemplate.buildUserComplete());
        doReturn(users).when(userRepository).findAll();

        var output = userService.listAllUsers();

        assertNotNull(output);
        assertFalse(output.isEmpty());
        assertEquals(users.size(), output.size());
    }

    @Test
    void listAllUsers_whenUserNOtExists_expectedReturnUserEmptyList() {
        doReturn(Collections.emptyList()).when(userRepository).findAll();
        var output = userService.listAllUsers();

        assertNotNull(output);
        assertTrue(output.isEmpty());
    }

    @Test
    void inactiveUserByCpf_whenUserIsActive_expectedReturnSuccessMessage() {
        var cpf = "12345678901";
        var user = UserTemplate.buildUserComplete();
        user.setCpf(cpf);
        user.setActive(true);

        doReturn("User with CPF " + cpf + " has been successfully inactivated.")
                .when(activeOrInactiveUserComponent)
                .changeUserStatus(cpf, false,
                        "User with CPF {cpf} is already inactive.",
                        "User with CPF {cpf} has been successfully inactivated.");

        var output = userService.inactiveUserByCpf(cpf);
        assertNotNull(output);
        assertEquals("User with CPF " + cpf + " has been successfully inactivated.", output);
    }

    @Test
    void reactiveUserByCpf_whenUserIsInactive_expectedReturnSuccessMessage() {
        var cpf = "12345678901";
        var user = UserTemplate.buildUserComplete();
        user.setCpf(cpf);
        user.setActive(false);

        doReturn("User with CPF " + cpf + " has been successfully reactivated.")
                .when(activeOrInactiveUserComponent)
                .changeUserStatus(cpf, true,
                        "User with CPF {cpf} is already active.",
                        "User with CPF {cpf} has been successfully reactivated.");

        var output = userService.reactiveUserByCpf(cpf);
        assertNotNull(output);
        assertEquals("User with CPF " + cpf + " has been successfully reactivated.", output);
    }

    @Test
    void deleteByCpf_whenUserIsFound_expectedSuccess() {
        var cpf = "12345678901";
        var user = UserTemplate.buildUserComplete();
        user.setCpf(cpf);

        doReturn(Optional.of(user)).when(userRepository).findByCpf(cpf);
        doNothing().when(userRepository).delete(any());

        userService.deleteByCpf(cpf);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteByCpf_whenUserIsNotFound_expectedNotFoundException() {
        var cpf = "12345678901";

        doReturn(Optional.empty()).when(userRepository).findByCpf(cpf);
        assertThrows(EntityNotFoundException.class, () -> userService.deleteByCpf(cpf));
    }

    @Test
    void deleteByCpf_whenErrorOccurs_expectedEntityErrorException() {
        var cpf = "12345678901";
        var user = UserTemplate.buildUserComplete();
        user.setCpf(cpf);

        doReturn(Optional.of(user)).when(userRepository).findByCpf(cpf);
        doThrow(new EntityErrorException("Error to delete this user: ", cpf, new RuntimeException())).when(userRepository).delete(any());
        assertThrows(EntityErrorException.class, () -> userService.deleteByCpf(cpf));
    }

    @Test
    void updatedUserByCpf_whenUserIsFound_expectedSuccess() {
        var request = CreateUserRequestTemplate.buildRequestComplete();
        var cpf = request.cpf();
        var user = UserTemplate.buildUserComplete();
        user.setCpf(cpf);

        doReturn(Optional.of(user)).when(userRepository).findByCpf(cpf);
        doReturn("encodedPassword").when(passwordEncoder).encode(any());
        doReturn(user).when(userRepository).save(any());

        userService.updatedUserByCpf(request);

        verify(userRepository, times(1)).save(user);
        assertEquals(request.email(), user.getEmail());
        assertEquals(request.name(), user.getName());
        assertEquals(true, user.getActive());
    }

    @Test
    void updatedUserByCpf_whenUserIsNotFound_expectedThrowUserNotFoundException() {
        var request = CreateUserRequestTemplate.buildRequestComplete();
        var cpf = request.cpf();

        doReturn(Optional.empty()).when(userRepository).findByCpf(cpf);
        assertThrows(EntityNotFoundException.class, () -> userService.updatedUserByCpf(request));
    }
}
