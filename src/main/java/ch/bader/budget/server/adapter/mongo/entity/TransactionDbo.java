package ch.bader.budget.server.adapter.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("transaction")
public class TransactionDbo {

    @Id
    private String id;
    private String creditedAccountId;
    private String debitedAccountId;
    private LocalDate date;
    private String description;
    private ValueEnumDbo paymentStatus;
    private ValueEnumDbo indication;
    private ValueEnumDbo paymentType;
    private BigDecimal budgetedAmount;
    private BigDecimal effectiveAmount;
    private LocalDateTime creationDate;
}
