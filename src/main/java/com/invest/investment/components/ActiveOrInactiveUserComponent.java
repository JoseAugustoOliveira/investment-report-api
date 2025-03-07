package com.invest.investment.components;

import com.invest.investment.exceptions.EntityNotFoundException;
import com.invest.investment.repositories.mongo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActiveOrInactiveUserComponent {

    private final UserRepository userRepository;

    public String changeUserStatus(String cpf, boolean status, String alreadyMessage, String successMessage) {
        var user = userRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("User not found by CPF: " + cpf));

        if (user.getActive() == status) {
            return alreadyMessage.replace("{cpf}", cpf);
        }

        user.setActive(status);
        user.setUpdateTimestamp(Instant.now());
        userRepository.save(user);

        return successMessage.replace("{cpf}", cpf);
    }
}