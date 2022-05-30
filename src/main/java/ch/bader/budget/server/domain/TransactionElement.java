package ch.bader.budget.server.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionElement implements Comparable<TransactionElement> {

    private String name;
    private BigDecimal amount;
    private BigDecimal budgetedAmount;
    private BigDecimal balance;
    private BigDecimal budgetedBalance;
    private Boolean hasAmount;
    private String id;

    public String getName() {
        return name;
    }

    public TransactionElement(Transaction transaction, BigDecimal amount, BigDecimal budgetedAmount,
                              BigDecimal balance,
                              BigDecimal budgetedBalance) {
        this.name = transaction.getDescription();
        this.id = transaction.getId();
        this.amount = amount;
        this.budgetedAmount = budgetedAmount;
        this.balance = balance;
        this.budgetedBalance = budgetedBalance;
        this.hasAmount = true;
    }

    public TransactionElement(String name, BigDecimal balance, BigDecimal budgetedBalance, String id) {
        this.name = name;
        this.balance = balance;
        this.budgetedBalance = budgetedBalance;
        this.hasAmount = false;
        this.amount = BigDecimal.ZERO;
        this.budgetedAmount = BigDecimal.ZERO;
        this.id = id;
    }

    public TransactionElement(BigDecimal in, BigDecimal out, BigDecimal budgetedIn, BigDecimal budgetedOut) {
        this.name = "In/Out";
        this.balance = out;
        this.budgetedBalance = budgetedOut;
        this.hasAmount = true;
        this.amount = in;
        this.budgetedAmount = budgetedIn;
        this.id = String.valueOf(Integer.MAX_VALUE);
    }

    @Override
    public int compareTo(TransactionElement o) {
        return this.id.compareTo(o.id);

    }
}
