package ch.bader.budget.server.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransactionIndication implements ValueEnum<Integer> {

	EXPECTED(1, "E"), UNEXPECTED(2, "U");

	private Integer value;

	private String name;

	private TransactionIndication(int value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public String getName() {
		return name;
	}

	@JsonCreator
	public static TransactionIndication forValues(@JsonProperty("name") String name,
			@JsonProperty("value") double value) {
		for (TransactionIndication indication : TransactionIndication.values()) {
			if (indication.name.equals(name) && Double.compare(indication.value, value) == 0) {
				return indication;
			}
		}

		return null;
	}

}
