package ch.bader.budget.server.type;

public enum AccountType implements ValueEnum<Integer> {

	CHECKING(1), SAVING(2, 1), CREDIT(3), FOREIGN(4);

	private Integer value;

	private Integer transactionLimit;

	private AccountType(int value) {
		this.value = value;
		this.transactionLimit = 0;
	}

	private AccountType(int value, int transactionLimit) {
		this.value = value;
		this.transactionLimit = transactionLimit;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	public Integer getTransactionLimit() {
		return transactionLimit;
	}

}
