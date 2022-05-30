package ch.bader.budget.server.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public enum PaymentStatus implements ValueEnum<Integer> {

    OPEN(1, "open", "red"), SETUP(2, "set up", "yellow"), PAID(3, "paid", "green");

    private final String color;

    private final String name;

    private final Integer value;

    PaymentStatus(int value, String name, String color) {
        this.value = value;
        this.name = name;
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }


    public static PaymentStatus forValue(Integer value) {
        return Arrays.stream(PaymentStatus.values()).filter(p -> p.getValue().equals(value)).findFirst().orElseThrow();
    }

    @JsonCreator
    public static PaymentStatus forValues(@JsonProperty("name") String name, @JsonProperty("value") double value,
                                          @JsonProperty("color") String color) {
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            if (paymentStatus.name.equals(name) && Double.compare(paymentStatus.value, value) == 0
                    && paymentStatus.color.equals(color)) {
                return paymentStatus;
            }
        }

        return null;
    }

}
