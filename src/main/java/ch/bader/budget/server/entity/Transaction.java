package ch.bader.budget.server.entity;

import ch.bader.budget.server.type.EnumUtil;
import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction implements Comparable<Transaction> {

    @Id
    @SequenceGenerator(name = "transaction_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    private Integer id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credited_account_id")
    private VirtualAccount creditedAccount;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "debited_account_id")
    private VirtualAccount debitedAccount;

    @NonNull
    private LocalDate date;

    @NonNull
    private String description;

    @Basic
    private int paymentStatusValue;

    @Transient
    private PaymentStatus paymentStatus;

    @Basic
    private int indicationValue;

    @Transient
    private TransactionIndication indication;

    @Basic
    private int paymentTypeValue;

    @Transient
    private PaymentType paymentType;

    private BigDecimal budgetedAmount;

    private BigDecimal effectiveAmount;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    @PostLoad
    void fillTransient() {
        if (paymentStatusValue > 0) {
            this.paymentStatus = EnumUtil.getEnumForValue(PaymentStatus.class, paymentStatusValue);
        }
        if (paymentTypeValue > 0) {
            this.paymentType = EnumUtil.getEnumForValue(PaymentType.class, paymentTypeValue);
        }
        if (indicationValue > 0) {
            this.indication = EnumUtil.getEnumForValue(TransactionIndication.class, indicationValue);
        }
    }

    @PrePersist
    void fillPersistent() {
        if (paymentStatus != null) {
            this.paymentStatusValue = paymentStatus.getValue();
        }
        if (paymentType != null) {
            this.paymentTypeValue = paymentType.getValue();
        }
        if (indication != null) {
            this.indicationValue = indication.getValue();
        }
        creationDate = LocalDateTime.now();
    }

    @PreUpdate
    void fillUpdate() {
        updateDate = LocalDateTime.now();
    }

    @Override
    public int compareTo(Transaction o) {
        return this.id.compareTo(o.id);

    }

    public void updateEnums() {
        fillPersistent();
    }

    public Transaction createDuplicate(LocalDate newDate) {
        Transaction newTransaction = new Transaction();
        newTransaction.creditedAccount = this.creditedAccount;
        newTransaction.debitedAccount = this.debitedAccount;
        newTransaction.description = this.description;
        newTransaction.paymentStatus = this.paymentStatus;
        newTransaction.indication = this.indication;
        newTransaction.paymentType = this.paymentType;
        newTransaction.budgetedAmount = this.budgetedAmount;
        newTransaction.effectiveAmount = this.effectiveAmount;

        newTransaction.date = newDate;

        return newTransaction;
    }


    public boolean isNotPrebudgetedAccount() {
        return !this.creditedAccount.isPrebudgetedAccount() && !this.debitedAccount.isPrebudgetedAccount();
    }
}
