package ch.bader.budget.server.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.bader.budget.server.entity.Transaction;
import ch.bader.budget.server.entity.VirtualAccount;
import ch.bader.budget.server.exception.NoAccountException;
import ch.bader.budget.server.repository.TransactionRepository;
import ch.bader.budget.server.repository.VirtualAccountRepository;
import ch.bader.budget.server.type.EnumUtil;
import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;

@RestController
@RequestMapping("/budget/transaction/")
public class TransactionController {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	VirtualAccountRepository virtualAccountRepository;

	@PostMapping(path = "/create")
	@ResponseStatus(HttpStatus.CREATED)
	public void createTransaction(@RequestParam LocalDate date, @RequestParam int creditedAccountId,
			@RequestParam int debitedAccountId, @RequestParam float budgetedAmount, @RequestParam float effectiveAmount,
			@RequestParam int transactionIdication, @RequestParam int paymentStatus, @RequestParam int paymentType)
			throws NoAccountException {

		Optional<VirtualAccount> optionalDebitedAccount = virtualAccountRepository.findById(debitedAccountId);
		Optional<VirtualAccount> optionaCreditedAccount = virtualAccountRepository.findById(creditedAccountId);
		if (optionaCreditedAccount.isEmpty() || optionalDebitedAccount.isEmpty()) {
			throw new NoAccountException(optionaCreditedAccount.isEmpty() ? debitedAccountId : creditedAccountId);
		}
		Transaction transaction = new Transaction(optionaCreditedAccount.get(), optionalDebitedAccount.get(), date);
		transaction.setBudgetedAmount(budgetedAmount);
		transaction.setEffectiveAmount(effectiveAmount);
		transaction.setIndication(EnumUtil.getEnumForValue(TransactionIndication.class, transactionIdication));
		transaction.setPaymentStatus(EnumUtil.getEnumForValue(PaymentStatus.class, paymentStatus));
		transaction.setPaymentType(EnumUtil.getEnumForValue(PaymentType.class, paymentType));
		transactionRepository.save(transaction);
	}

	@GetMapping(path = "/list")
	public Iterable<Transaction> getAllTransactions() {
		return transactionRepository.findAll();
	}

	@GetMapping(path = "/list")
	public Iterable<Transaction> getAllTransactions(@RequestParam LocalDate from, @RequestParam LocalDate to) {
		return transactionRepository.findAllTransactionsInInterval(from, to);
	}

}
