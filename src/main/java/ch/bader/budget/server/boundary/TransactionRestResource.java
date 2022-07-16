package ch.bader.budget.server.boundary;

import ch.bader.budget.server.boundary.dto.TransactionBoundaryDto;
import ch.bader.budget.server.boundary.dto.TransactionElementBoundaryDto;
import ch.bader.budget.server.core.transaction.TransactionService;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.TransactionElement;
import ch.bader.budget.server.mapper.TransactionElementMapper;
import ch.bader.budget.server.mapper.TransactionMapper;
import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/budget/transaction/")
public class TransactionRestResource {

    @Autowired
    TransactionService transactionService;

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    TransactionElementMapper transactionElementMapper;

    @PostMapping(path = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionBoundaryDto createTransaction(@RequestBody TransactionBoundaryDto dto) {
        Transaction transaction = transactionMapper.mapToDomain(dto);
        transaction = transactionService.createTransaction(transaction);
        return transactionMapper.mapToDto(transaction);
    }

    @PutMapping(path = "/update")
    public TransactionBoundaryDto updateTransaction(@RequestBody TransactionBoundaryDto dto) {
        Transaction transaction = transactionMapper.mapToDomain(dto);
        transaction = transactionService.updateTransaction(transaction);
        return transactionMapper.mapToDto(transaction);
    }

    @DeleteMapping(path = "/delete")
    public void deleteTransaction(@RequestParam String transactionId) {
        transactionService.deleteTransaction(transactionId);
    }

    @PostMapping(path = "/dublicate")
    @ResponseStatus(HttpStatus.CREATED)
    public void dublicateTransaction(@RequestBody TransactionBoundaryDto dto) {
        Transaction transaction = transactionMapper.mapToDomain(dto);
        transactionService.dublicateTransaction(transaction);
    }

    @GetMapping(path = "/")
    public ResponseEntity<TransactionBoundaryDto> getTransactionById(@RequestParam String id) {
        try {
            Transaction transaction = transactionService.getTransactionById(id);
            return ResponseEntity.ok(transactionMapper.mapToDto(transaction));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @GetMapping(path = "/list")
    public List<TransactionBoundaryDto> getAllTransactions(@RequestParam(name = "date") long dateLong) {
        LocalDate date = Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        List<Transaction> transactions = transactionService.getAllTransactions(date);
        return transactions.stream().map(transactionMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping(path = "/listByMonthAndVirtualAccount")
    public List<TransactionElementBoundaryDto> getAllTransactionsForMonthAndVirtualAccount(
        @RequestParam(name = "date") long dateLong,
        @RequestParam String accountId) {
        LocalDate date = Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        List<TransactionElement> transactionElements = transactionService.getAllTransactionsForMonthAndVirtualAccount(
            date,
            accountId);

        return transactionElements.stream()
                                  .map(transactionElementMapper::mapToDto)
                                  .collect(Collectors.toList());
    }

    @GetMapping(path = "/listByMonthAndRealAccount")
    public List<TransactionElementBoundaryDto> getAllTransactionsForMonthAndRealAccount(
        @RequestParam(name = "date") long dateLong,
        @RequestParam String accountId) {
        LocalDate date = Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1);
        return transactionService.getAllTransactionsForMonthAndRealAccount(date, accountId)
                                 .stream()
                                 .map(transactionElementMapper::mapToDto)
                                 .collect(
                                     Collectors.toList());
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
