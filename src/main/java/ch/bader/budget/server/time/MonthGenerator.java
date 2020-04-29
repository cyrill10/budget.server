package ch.bader.budget.server.time;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonthGenerator {

	public final static LocalDate STARTDATE = LocalDate.of(2020, 4, 1);

	public static List<LocalDate> getallMonths() {
		LocalDate today = LocalDate.now();
		LocalDate todayInAYear = today.plusYears(1).withDayOfMonth(1);

		List<LocalDate> allMonths = new ArrayList<LocalDate>();
		LocalDate startDate = LocalDate.from(STARTDATE);
		while (startDate.isBefore(todayInAYear)) {
			allMonths.add(startDate);
			startDate = startDate.plusMonths(1);
		}
		return allMonths;
	}

}
