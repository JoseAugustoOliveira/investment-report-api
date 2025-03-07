package com.invest.investment.entites.postgres;

import com.invest.investment.enums.State;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ENDERECO_COBRANCA")
public class BillingAddress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PK")
    private Long id;

    @Column(name = "LOGRADOURO")
    private String street;

    @Column(name = "NUMERO")
    private String number;

    @Column(name = "BAIRRO")
    private String neighbourhood;

    @Column(name = "CIDADE")
    private String city;

    @Column(name = "ESTADO")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "DT_COBRANCA")
    private LocalDate billingDate;

    // TODO: Cascade all
    @OneToOne
    @JoinColumn(name = "ID_PK")
    private Account account;
}
