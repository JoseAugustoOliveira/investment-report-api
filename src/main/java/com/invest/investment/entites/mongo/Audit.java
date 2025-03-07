package com.invest.investment.entites.mongo;

import com.invest.investment.enums.Operation;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "TB_AUDITORIA_INVEST")
public class Audit {

    @Id
    private String id;

    private LocalDateTime createdDate;

    private String nameUser;

    private String documentNumberUser;

    private Operation operation;
}
