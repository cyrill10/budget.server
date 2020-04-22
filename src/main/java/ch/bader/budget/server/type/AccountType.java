package ch.bader.budget.server.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum AccountType implements ValueEnum<Integer> {

	CHECKING(1, "Checking"), SAVING(2, "Saving", 1), CREDIT(3, "Credit"), ALIEN(4, "Alien");

	private Integer value;

	private String name;

	private Integer transactionLimit;

	private AccountType(int value, String name) {
		this.value = value;
		this.name = name;
		this.transactionLimit = 0;
	}

	private AccountType(int value, String name, int transactionLimit) {
		this.value = value;
		this.name = name;
		this.transactionLimit = transactionLimit;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public String getName() {
		return name;
	}

	public Integer getTransactionLimit() {
		return transactionLimit;
	}

	@JsonCreator
	public static AccountType forValues(@JsonProperty("name") String name, @JsonProperty("value") double value,
			@JsonProperty("transactionLimit") double transactionLimit) {
		for (AccountType accountType : AccountType.values()) {
			if (accountType.name.equals(name) && Double.compare(accountType.value, value) == 0
					&& Double.compare(accountType.transactionLimit, transactionLimit) == 0) {
				return accountType;
			}
		}

		return null;
	}

}
