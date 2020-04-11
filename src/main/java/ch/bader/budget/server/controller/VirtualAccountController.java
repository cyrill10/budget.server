package ch.bader.budget.server.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.bader.budget.server.entity.RealAccount;
import ch.bader.budget.server.entity.VirtualAccount;
import ch.bader.budget.server.exception.NoAccountException;
import ch.bader.budget.server.repository.RealAccountRepository;
import ch.bader.budget.server.repository.VirtualAccountRepository;
import ch.bader.budget.server.type.AccountType;
import ch.bader.budget.server.type.EnumUtil;

@RestController
@RequestMapping("/budget/virtualAccount/")
public class VirtualAccountController {

	@Autowired
	private VirtualAccountRepository virtualAccountRepository;

	@Autowired
	private RealAccountRepository realAccountRepository;

	@PostMapping(path = "/add")
	@ResponseStatus(HttpStatus.CREATED)
	public void addNewAccount(@RequestParam String name, @RequestParam int accountType, @RequestParam int realAccountId)
			throws NoAccountException {
		VirtualAccount account = new VirtualAccount();
		account.setName(name);
		account.setAccountType(EnumUtil.getEnumForValue(AccountType.class, accountType));
		Optional<RealAccount> underlyingAccountOptional = realAccountRepository.findById(realAccountId);
		if (underlyingAccountOptional.isEmpty()) {
			throw new NoAccountException(realAccountId);
		}
		account.setUnderlyingAccount(underlyingAccountOptional.get());
		virtualAccountRepository.save(account);
	}

	@GetMapping(path = "/list")
	public Iterable<VirtualAccount> getAllAccounts() {
		return virtualAccountRepository.findAll();
	}
}
