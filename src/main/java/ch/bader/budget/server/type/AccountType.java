package ch.bader.budget.server.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public enum AccountType implements ValueEnum<Integer> {

    CHECKING(1, "Checking"), SAVING(2, "Saving"), CREDIT(3, "Credit"), ALIEN(4, "Alien"),
    PREBUDGETED(5, "Prebudgeted");

    private static final AccountType[] internalTypes = {CHECKING, SAVING, CREDIT};

    private static final AccountType[] overviewTypes = {CHECKING, SAVING, CREDIT, PREBUDGETED};

    private static final AccountType[] PrebudgetedTypes = {PREBUDGETED};

    private static final AccountType[] alienTypes = {ALIEN};

    private final Integer value;

    private final String name;

    AccountType(int value, String name) {
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

    public static AccountType forValue(Integer value) {
        return Arrays.stream(AccountType.values()).filter(p -> p.getValue().equals(value)).findFirst().orElseThrow();
    }

    @JsonCreator
    public static AccountType forValues(@JsonProperty("name") String name, @JsonProperty("value") double value) {
        for (AccountType accountType : AccountType.values()) {
            if (accountType.name.equals(name) && Double.compare(accountType.value, value) == 0) {
                return accountType;
            }
        }

        return null;
    }

    public boolean isInternalAccount() {
        return Arrays.asList(internalTypes).contains(this);
    }

    public boolean isPrebudgetedAccount() {
        return Arrays.asList(PrebudgetedTypes).contains(this);
    }

    public boolean isAlienAccount() {
        return Arrays.asList(alienTypes).contains(this);
    }

    public boolean isOverviewAccount() {
        return Arrays.asList(overviewTypes).contains(this);
    }

}
