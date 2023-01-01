package ch.bader.budget.server.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionListElement implements Comparable<TransactionListElement> {

    private String name;
    private BigDecimal effectiveAmount;
    private BigDecimal budgetedAmount;
    private BigDecimal effectiveBalance;
    private BigDecimal budgetedBalance;
    private Boolean hasAmount;
    private String id;

    public TransactionListElement(Transaction transaction, BigDecimal effectiveAmount,
                                  BigDecimal budgetedAmount,
                                  BigDecimal effectiveBalance,
                                  BigDecimal budgetedBalance) {
        this.name = transaction.getDescription();
        this.id = transaction.getId();
        this.effectiveAmount = effectiveAmount;
        this.budgetedAmount = budgetedAmount;
        this.effectiveBalance = effectiveBalance;
        this.budgetedBalance = budgetedBalance;
        this.hasAmount = true;
    }

    public TransactionListElement(String name, BigDecimal effectiveBalance,
                                  BigDecimal budgetedBalance, String id) {
        this.name = name;
        this.effectiveBalance = effectiveBalance;
        this.budgetedBalance = budgetedBalance;
        this.hasAmount = false;
        this.effectiveAmount = BigDecimal.ZERO;
        this.budgetedAmount = BigDecimal.ZERO;
        this.id = id;
    }

    public TransactionListElement(BigDecimal alienTransactionEffective,
                                  BigDecimal alienTransactionBudgeted) {
        this.name = "In/Out";
        this.effectiveBalance = alienTransactionEffective;
        this.budgetedBalance = alienTransactionBudgeted;
        this.hasAmount = false;
        this.id = String.valueOf(Integer.MAX_VALUE);
    }

    @Override
    public int compareTo(TransactionListElement o) {
        return this.id.compareTo(o.id);

    }
}
