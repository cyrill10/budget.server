package ch.bader.budget.server.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.bader.budget.server.calculation.OverviewCalculator;
import ch.bader.budget.server.calculation.implementation.predicate.InternalAccountPredicate;
import ch.bader.budget.server.entity.RealAccount;
import ch.bader.budget.server.json.OverviewElement;
import ch.bader.budget.server.repository.RealAccountRepository;

@RestController
@RequestMapping("/budget/overview/")
public class OverviewController {

	@Autowired
	private RealAccountRepository realAccountRepository;

	@GetMapping(path = "/list")
	public Iterable<OverviewElement> getAllTransactions(@RequestParam long dateLong) {
		LocalDate date = Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault()).toLocalDate();
		List<RealAccount> accounts = realAccountRepository.findAll().stream().filter(new InternalAccountPredicate())
				.collect(Collectors.toList());
		return OverviewCalculator.getOverviewForMonth(accounts, date.withDayOfMonth(1));
	}

}
