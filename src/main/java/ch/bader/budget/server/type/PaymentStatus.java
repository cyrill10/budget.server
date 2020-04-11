package ch.bader.budget.server.type;

public enum PaymentStatus implements ValueEnum<Integer> {

	OPEN(1, "#FF0000"), SETUP(2, "#FFFF00"), PAID(3, "#008000");

	private String colorCode;

	private Integer value;

	private PaymentStatus(int value, String colorCode) {
		this.value = value;
		this.colorCode = colorCode;
	}

	public String getColorCode() {
		return this.colorCode;
	}

	@Override
	public Integer getValue() {
		return value;
	}

}
