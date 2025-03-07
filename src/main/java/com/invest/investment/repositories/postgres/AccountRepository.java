package com.invest.investment.repositories.postgres;

import com.invest.investment.entites.postgres.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCpfOrContracteeDocumentNumber(String cpf, String contractDocumentNumber);
    Optional<Account> findByCpf(String cpf);
}
