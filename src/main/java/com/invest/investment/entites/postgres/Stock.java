package com.invest.investment.entites.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ESTOQUE")
public class Stock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ID_ACAO")
    private String stockId;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "QUANTIDADE")
    private Long quantity;

    @Column(name = "VR_COMPRA")
    private BigDecimal buyPrice;

    @Column(name = "VR_VENDA")
    private BigDecimal salePrice;

    @Column(name = "LUCRO")
    private BigDecimal gain;

    @CreationTimestamp
    @Column(name = "DT_COMPRA")
    private Instant buyDate;

    @Column(name = "DT_VENDA")
    private Instant saleDate;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID_PK")
    private Account account;
}
