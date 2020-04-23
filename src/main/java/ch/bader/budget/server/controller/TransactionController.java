package ch.bader.budget.server.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.bader.budget.server.entity.Transaction;
import ch.bader.budget.server.repository.TransactionRepository;
import ch.bader.budget.server.repository.VirtualAccountRepository;
import ch.bader.budget.server.time.MonthGenerator;
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

	@PostMapping(path = "/add")
	@ResponseStatus(HttpStatus.CREATED)
	public void createTransaction(@RequestBody Transaction transaction) {
		transactionRepository.save(transaction);
	}

	@GetMapping(path = "/list")
	public Iterable<Transaction> getAllTransactions() {
		return transactionRepository.findAll();
	}

	@GetMapping(path = "/listByMonthAndAccont")
	public Iterable<Transaction> getAllTransactionsForMonthAndAccount(@RequestParam LocalDate from,
			@RequestParam int accountId) {
		return transactionRepository.findAllTransactionsInInterval(from, from.plusMonths(1), accountId);
	}

	@GetMapping(path = "/type/list")
	public List<PaymentType> getAllPaymentTypes() {
		return Arrays.asList(PaymentType.values());
	}

	@GetMapping(path = "/indication/list")
	public List<TransactionIndication> getAllIndicationTypes() {
		return Arrays.asList(TransactionIndication.values());
	}

	@GetMapping(path = "/status/list")
	public List<PaymentStatus> getAllStatusTypes() {
		return Arrays.asList(PaymentStatus.values());
	}

	@GetMapping(path = "/month/list")
	public List<LocalDate> getAllMonths() {
		return MonthGenerator.getallMonths();
	}

}
