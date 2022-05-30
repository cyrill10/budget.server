package ch.bader.budget.server.adapter.sql.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "virtual_account")
public class VirtualAccountDboSql {

    @Id
    @SequenceGenerator(name = "virtual_account_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "virtual_account_seq")
    private Integer id;

    @NonNull
    private String name;

    private BigDecimal balance;

    private Boolean isDeleted;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "underlyingAccount_id")
    private RealAccountDboSql underlyingAccount;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creditedAccount")
    @JsonIgnore
    private List<TransactionDboSql> creditedTransactions;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "debitedAccount")
    @JsonIgnore
    private List<TransactionDboSql> debitedTransactions;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    @PrePersist
    void fillPersist() {
        creationDate = LocalDateTime.now();
    }

    @PreUpdate
    void fillUpdate() {
        updateDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return getCleanName();
    }

    public String getCleanName() {
        return this.getName() + " (" + this.getUnderlyingAccount().getName() + ")";
    }
}
