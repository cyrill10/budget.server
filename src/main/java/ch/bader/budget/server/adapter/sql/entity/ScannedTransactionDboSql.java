package ch.bader.budget.server.adapter.sql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scanned_transaction")
public class ScannedTransactionDboSql {

    @Id
    @SequenceGenerator(name = "scanned_transaction_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scanned_transaction_seq")
    private Integer id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "closingProcess_id")
    private ClosingProcessDboSql closingProcess;

    @PreUpdate
    void fillUpdate() {
        updateDate = LocalDateTime.now();
    }

    private String description;

    private LocalDate date;

    private BigDecimal amount;

    private Boolean transactionCreated;

    private LocalDateTime creationDate;

    private String cardType;

    private LocalDateTime updateDate;

    @PrePersist
    void fillPersist() {
        creationDate = LocalDateTime.now();
    }
}
