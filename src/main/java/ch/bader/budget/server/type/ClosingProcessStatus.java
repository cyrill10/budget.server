package ch.bader.budget.server.type;

import java.util.Arrays;

public enum ClosingProcessStatus implements ValueEnum<Integer> {
    NEW(0, "new"), STARTED(1, "open"), DONE(2, "done");

    private final Integer value;
    private final String name;

    ClosingProcessStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public static ClosingProcessStatus forValue(Integer value) {
        return Arrays.stream(ClosingProcessStatus.values())
                     .filter(p -> p.getValue().equals(value))
                     .findFirst()
                     .orElseThrow();
    }
}
