package ch.bader.budget.server.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.bader.budget.server.time.MonthGenerator;

@RestController
@RequestMapping("/budget/date/")
public class DateController {


	@GetMapping(path = "/month/list")
	public List<LocalDate> getAllMonths() {
		return MonthGenerator.getallMonths();
	}
}
