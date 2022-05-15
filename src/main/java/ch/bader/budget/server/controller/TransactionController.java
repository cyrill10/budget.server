package ch.bader.budget.server.controller;

import ch.bader.budget.server.boundary.TransactionElement;
import ch.bader.budget.server.calculation.TransactionCalculator;
import ch.bader.budget.server.entity.Transaction;
import ch.bader.budget.server.repository.RealAccountRepository;
import ch.bader.budget.server.repository.TransactionRepository;
import ch.bader.budget.server.repository.VirtualAccountRepository;
import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/budget/transaction/")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    VirtualAccountRepository virtualAccountRepository;

    @Autowired
    RealAccountRepository realAccountRepository;

    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @PutMapping(path = "/update")
    public Transaction updateTransaction(@RequestBody Transaction transaction) {
        transaction.updateEnums();
        return transactionRepository.save(transaction);
    }

    @DeleteMapping(path = "/delete")
    public void deleteTransaction(@RequestParam Integer transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    @PostMapping(path = "/dublicate")
    @ResponseStatus(HttpStatus.CREATED)
    public void dublicateTransaction(@RequestBody Transaction transaction) {
        LocalDate startDate = transaction.getDate();
        LocalDate endDate = transaction.getDate().plusYears(1).withDayOfMonth(1).withMonth(1);
        List<Transaction> newTransactions = new ArrayList<>();

        while (startDate.isBefore(endDate)) {
            startDate = startDate.plusMonths(1);
            newTransactions.add(transaction.createDuplicate(startDate));
        }
        transactionRepository.saveAll(newTransactions);
    }

    @GetMapping(path = "/")
    public Transaction getTransactionById(@RequestParam int id) {
        return transactionRepository.findById(id).orElseThrow();
    }

    @GetMapping(path = "/list")
    public Iterable<Transaction> getAllTransactions(@RequestParam long date) {
        LocalDate from = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        return transactionRepository.findAllTransactionsInInterval(from, from.plusMonths(1));
    }

    @GetMapping(path = "/listByMonthAndVirtualAccount")
    public Iterable<TransactionElement> getAllTransactionsForMonthAndVirtualAccount(@RequestParam long date,
                                                                                    @RequestParam int accountId) {
        LocalDate from = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        return TransactionCalculator.getTransactionsForMonth(virtualAccountRepository.findById(accountId).orElseThrow(),
                transactionRepository
                        .findAllTransactionsInIntervalForVirtualAccount(from, from.plusMonths(1), accountId).stream()
                        .sorted().collect(Collectors.toList()),
                from);
    }

    @GetMapping(path = "/listByMonthAndRealAccount")
    public Iterable<TransactionElement> getAllTransactionsForMonthAndRealAccount(@RequestParam long date,
                                                                                 @RequestParam int accountId) {
        LocalDate from = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        return TransactionCalculator.getTransactionsForMonth(
                transactionRepository.findAllTransactionsInIntervalForRealAccount(from, from.plusMonths(1), accountId)
                                     .stream().sorted().collect(Collectors.toList()),
                virtualAccountRepository.findAllByUnderlyingAccountId(accountId), from);
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

}
