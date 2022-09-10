package ch.bader.budget.server.type;

import java.util.Arrays;

public enum TransactionIndication implements ValueEnum<Integer> {

    EXPECTED(1, "E"), UNEXPECTED(2, "U");

    private final Integer value;

    private final String name;

    TransactionIndication(int value, String name) {
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

    public static TransactionIndication forValue(Integer value) {
        return Arrays.stream(TransactionIndication.values()).filter(p -> p.getValue().equals(value)).findFirst()
                     .orElseThrow();
    }

}
