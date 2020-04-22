package ch.bader.budget.server.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.bader.budget.server.entity.RealAccount;
import ch.bader.budget.server.repository.RealAccountRepository;
import ch.bader.budget.server.type.AccountType;

@RestController
@RequestMapping("/budget/realAccount/")
public class RealAccountController {

	@Autowired
	private RealAccountRepository realAccountRepository;

	@PostMapping(path = "/add")
	@ResponseStatus(HttpStatus.CREATED)
	public RealAccount addNewAccount(@RequestBody RealAccount account) {
		return realAccountRepository.save(account);
	}

	@GetMapping(path = "/list")
	public List<RealAccount> getAllAccounts() {
		List<RealAccount> accounts = realAccountRepository.findAll();
		return accounts.stream().sorted().collect(Collectors.toList());
	}

	@PutMapping(path = "/update")
	public RealAccount updateAccount(@RequestBody RealAccount account) {
		return realAccountRepository.save(account);
	}

	@GetMapping(path = "/type/list")
	public List<AccountType> getAllAccountTypes() {
		return Arrays.asList(AccountType.values());
	}
}
