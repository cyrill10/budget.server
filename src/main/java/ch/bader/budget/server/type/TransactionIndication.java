package ch.bader.budget.server.type;

public enum TransactionIndication implements ValueEnum<Integer> {

	EXPECTED(1), UNEXPECTED(2);

	private Integer value;

	private TransactionIndication(int value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}

}
