package com.invest.investment.components;

import com.invest.investment.entites.mongo.Audit;
import com.invest.investment.entites.mongo.User;
import com.invest.investment.enums.Operation;
import com.invest.investment.repositories.mongo.AuditRepository;
import com.invest.investment.repositories.mongo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.invest.investment.enums.Operation.fromOperationName;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditComponent {

    private final UserRepository userRepository;
    private final AuditRepository auditRepository;

    public void saveAudit(String cpf, Operation operation) {
        Audit audit = Audit.builder()
                .createdDate(LocalDateTime.now())
                .nameUser(getNameUser(cpf).get().getName())
                .documentNumberUser(cpf)
                .operation(fromOperationName(operation.getOperationName()))
                .build();
        auditRepository.save(audit);
    }

    private Optional<User> getNameUser(String cpf) {
        return userRepository.findByCpf(cpf);
    }
}