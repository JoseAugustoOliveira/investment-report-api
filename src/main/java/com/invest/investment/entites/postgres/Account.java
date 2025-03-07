package com.invest.investment.entites.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CONTA")
public class Account  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PK")
    private Long id;

    @Column(name = "NOME")
    private String name;

    @Column(name = "DESCRICAO")
    private String description;

    @Column(name = "NU_CNPJ")
    private String contracteeDocumentNumber;

    // TODO: OR
    @Column(name = "NU_CPF")
    private String cpf;

    @CreationTimestamp
    @Column(name = "DT_CRIACAO")
    private Instant creationTimeStamp;

    @OneToOne(mappedBy = "account")
    private BillingAddress billingAddress;

    @OneToMany(mappedBy = "account")
    private List<Stock> stocks;
}
