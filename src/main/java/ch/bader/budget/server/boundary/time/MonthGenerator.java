package ch.bader.budget.server.boundary.time;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonthGenerator {

    public static final LocalDate STARTDATE = LocalDate.of(2021, 12, 1);

    private MonthGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static List<LocalDate> getallMonths() {
        LocalDate today = LocalDate.now();
        LocalDate todayInAYear = today.plusYears(1).withDayOfMonth(1);

        List<LocalDate> allMonths = new ArrayList<>();
        LocalDate startDate = LocalDate.from(STARTDATE);
        while (startDate.isBefore(todayInAYear)) {
            allMonths.add(startDate);
            startDate = startDate.plusMonths(1);
        }
        return allMonths;
    }
}
