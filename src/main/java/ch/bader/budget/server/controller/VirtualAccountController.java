package ch.bader.budget.server.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.bader.budget.server.entity.VirtualAccount;
import ch.bader.budget.server.repository.VirtualAccountRepository;

@RestController
@RequestMapping("/budget/virtualAccount/")
public class VirtualAccountController {

	@Autowired
	private VirtualAccountRepository virtualAccountRepository;

	@PostMapping(path = "/add")
	@ResponseStatus(HttpStatus.CREATED)
	public VirtualAccount addNewAccount(@RequestBody VirtualAccount virtualAccount) {
		return virtualAccountRepository.save(virtualAccount);
	}

	@PutMapping(path = "/update")
	public VirtualAccount updateAccount(@RequestBody VirtualAccount account) {
		return virtualAccountRepository.save(account);
	}

	@GetMapping(path = "/list")
	public Iterable<VirtualAccount> getAllAccounts() {
		List<VirtualAccount> accounts = virtualAccountRepository.findAll();
		return accounts.stream().sorted().collect(Collectors.toList());
	}

	@GetMapping(path = "/listForAccount")
	public Iterable<VirtualAccount> getAllAccountsForAccount(@RequestParam Integer realAccountId) {
		List<VirtualAccount> accounts = virtualAccountRepository.findAllByUnderlyingAccountId(realAccountId);
		return accounts.stream().sorted().collect(Collectors.toList());
	}
}
