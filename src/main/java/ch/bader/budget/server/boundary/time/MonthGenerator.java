package ch.bader.budget.server.boundary.time;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MonthGenerator {

    @Value("${budget.startdate}")
    private String startdate;

    public LocalDate getStartDate() {
        return LocalDate.parse(startdate);
    }

    public List<LocalDate> getallMonths() {
        LocalDate today = LocalDate.now();
        LocalDate todayInAYear = today.plusYears(1).withDayOfMonth(1);

        List<LocalDate> allMonths = new ArrayList<>();
        LocalDate startDate = getStartDate();
        while (startDate.isBefore(todayInAYear)) {
            allMonths.add(startDate);
            startDate = startDate.plusMonths(1);
        }
        return allMonths;
    }
}
