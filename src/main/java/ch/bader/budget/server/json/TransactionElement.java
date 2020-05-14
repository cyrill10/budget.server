package ch.bader.budget.server.json;

import ch.bader.budget.server.entity.Transaction;

public class TransactionElement implements Comparable<TransactionElement> {

	private String name;
	private Float amount;
	private Float budgetedAmount;
	private Float balance;
	private Float budgetedBalance;
	private boolean hasAmount;
	private Integer id;

	public String getName() {
		return name;
	}

	public TransactionElement(Transaction transaction, float amount, float budgetedAmount, float balance,
			Float budgetedBalance) {
		this.name = transaction.getDescription();
		this.id = transaction.getId();
		this.amount = amount;
		this.budgetedAmount = budgetedAmount;
		this.balance = balance;
		this.budgetedBalance = budgetedBalance;
		this.hasAmount = true;
	}

	public TransactionElement(String name, float balance, Float budgetedBalance) {
		this.name = name;
		this.balance = balance;
		this.budgetedBalance = budgetedBalance;
		this.hasAmount = false;
		this.amount = 0f;
		this.budgetedAmount = 0f;
		this.id = 0;
	}

	public boolean isHasAmount() {
		return hasAmount;
	}

	public float getAmount() {
		return amount;
	}

	public float getBudgetedAmount() {
		return budgetedAmount;
	}

	public float getBalance() {
		return balance;
	}

	public Float getBudgetedBalance() {
		return budgetedBalance;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public int compareTo(TransactionElement o) {
		return this.name.compareTo(o.name);

	}
}
