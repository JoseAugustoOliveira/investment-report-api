package com.invest.investment.repositories.postgres;

import com.invest.investment.entites.postgres.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
    List<Stock> findByAccount_Cpf(String cpf);
    Optional<Stock> findByStockId(String stockId);
    Page<Stock> findByAccount_Cpf(String cpf, Pageable pageable);

    @Query("SELECT s FROM Stock s WHERE s.account.cpf = :cpf AND s.buyDate BETWEEN :start AND :end")
    List<Stock> findByCpfAndCreatedAtBetween(String cpf, Instant start, Instant end);

}
