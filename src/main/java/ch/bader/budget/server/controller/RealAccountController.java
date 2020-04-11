package ch.bader.budget.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.bader.budget.server.entity.RealAccount;
import ch.bader.budget.server.repository.RealAccountRepository;
import ch.bader.budget.server.type.AccountType;
import ch.bader.budget.server.type.EnumUtil;

@RestController
@RequestMapping("/budget/realAccount/")
public class RealAccountController {

	@Autowired
	private RealAccountRepository realAccountRepository;

	@PostMapping(path = "/add")
	@ResponseStatus(HttpStatus.CREATED)
	public void addNewAccount(@RequestParam String name, @RequestParam int accountType) {
		RealAccount account = new RealAccount();
		account.setName(name);
		account.setAccountType(EnumUtil.getEnumForValue(AccountType.class, accountType));
		realAccountRepository.save(account);
	}

	@GetMapping(path = "/list")
	public Iterable<RealAccount> getAllAccounts() {
		return realAccountRepository.findAll();
	}
}
