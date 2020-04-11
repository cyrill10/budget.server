package ch.bader.budget.server.type;

public enum PaymentType implements ValueEnum<Integer> {

	DIRECT(1), STANDING_ODRDER(2), EBILL(3);

	private Integer value;

	private PaymentType(int value) {
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}

}
