package ch.bader.budget.server.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public enum PaymentType implements ValueEnum<Integer> {

    DEPOSIT(1, "Deposit"), STANDING_ORDER(2, "Standing Order"), EBILL(3, "e-bill");

    private final Integer value;

    private final String name;

    PaymentType(int value, String name) {
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

    public static PaymentType forValue(Integer value) {
        return Arrays.stream(PaymentType.values()).filter(p -> p.getValue().equals(value)).findFirst().orElseThrow();
    }

    @JsonCreator
    public static PaymentType forValues(@JsonProperty("name") String name, @JsonProperty("value") double value) {
        for (PaymentType paymentType : PaymentType.values()) {
            if (paymentType.name.equals(name) && Double.compare(paymentType.value, value) == 0) {
                return paymentType;
            }
        }

        return null;
    }

}
