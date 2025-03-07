package com.invest.investment.services;

import com.invest.investment.builders.CreatedUserResponseBuilder;
import com.invest.investment.builders.UserBuilder;
import com.invest.investment.components.ActiveOrInactiveUserComponent;
import com.invest.investment.entites.mongo.User;
import com.invest.investment.exceptions.EntityErrorException;
import com.invest.investment.exceptions.EntityNotFoundException;
import com.invest.investment.models.requests.CreateUserRequest;
import com.invest.investment.models.responses.CreatedUserResponse;
import com.invest.investment.repositories.mongo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final ActiveOrInactiveUserComponent activeOrInactiveUserComponent;

    public CreatedUserResponse createUser(CreateUserRequest request) {
        var newUser = UserBuilder.builder(
                request,
                passwordEncoder.encode(request.password()));

        userRepository.save(newUser);
        log.info("User registered with successfully, with name {}", newUser.getName());
        return CreatedUserResponseBuilder.build(newUser);
    }

    public CreatedUserResponse getUserByCpf(String cpf) {
        var user = userRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("User not found by cpf."));
        return mapToResponse(user);
    }

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public String inactiveUserByCpf(String cpf) {
        return activeOrInactiveUserComponent.changeUserStatus(
                cpf, false,
                "User with CPF {cpf} is already inactive.",
                "User with CPF {cpf} has been successfully inactivated."
        );
    }

    public String reactiveUserByCpf(String cpf) {
        return activeOrInactiveUserComponent.changeUserStatus(
                cpf, true,
                "User with CPF {cpf} is already active.",
                "User with CPF {cpf} has been successfully reactivated."
        );
    }

    public void deleteByCpf(String cpf) {
        var user = userRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("User not found by CPF: " + cpf));

        try {
            userRepository.delete(user);
            log.info("User with CPF {} deleted successfully.", cpf);

        } catch (EntityErrorException ex) {
            user.setUpdateTimestamp(Instant.now());
            throw new EntityErrorException("Error to delete user with cpf {}: ", cpf, ex.getCause());
        }
    }

    public void updatedUserByCpf(CreateUserRequest request) {
        var existsUser = userRepository.findByCpf(request.cpf());

        if (existsUser.isEmpty()) {
            throw new EntityNotFoundException("User not found for this cpf" + request.cpf());
        }

        var user = existsUser.get();

        user.setEmail(request.email());
        user.setCpf(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setActive(true);
        user.setUpdateTimestamp(Instant.now());

        log.info("User updated with name successfully: {}", user.getName());
        userRepository.save(user);
    }

    private CreatedUserResponse mapToResponse(User user) {
        return CreatedUserResponseBuilder.build(user);
    }
}
