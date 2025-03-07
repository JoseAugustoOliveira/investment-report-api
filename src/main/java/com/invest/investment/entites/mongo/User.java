package com.invest.investment.entites.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "TB_USUARIO_INVEST")
public class User {

    @MongoId
    private UUID id = UUID.randomUUID();

    @Indexed(name = "NAME")
    private String name;

    @Indexed(name = "CPF", unique = true)
    private String cpf;

    private String email;

    private Boolean active;

    private String password;

    @CreationTimestamp
    private Instant creationTimeStamp;

    @UpdateTimestamp
    private Instant updateTimestamp;
}
