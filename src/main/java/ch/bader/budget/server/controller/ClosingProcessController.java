package ch.bader.budget.server.controller;


import ch.bader.budget.server.boundary.SaveScannedTransactionBoundaryDto;
import ch.bader.budget.server.entity.ClosingProcess;
import ch.bader.budget.server.entity.ScannedTransaction;
import ch.bader.budget.server.entity.Transaction;
import ch.bader.budget.server.entity.VirtualAccount;
import ch.bader.budget.server.process.closing.ScannedTransactionBean;
import ch.bader.budget.server.repository.ClosingProcessRepository;
import ch.bader.budget.server.repository.ScannedTransactionRepository;
import ch.bader.budget.server.repository.TransactionRepository;
import ch.bader.budget.server.repository.VirtualAccountRepository;
import ch.bader.budget.server.type.ClosingProcessStatus;
import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/budget/closingProcess")
public class ClosingProcessController {

    @Autowired
    ClosingProcessRepository closingProcessRepository;

    @Autowired
    ScannedTransactionRepository scannedTransactionRepository;

    @Autowired
    VirtualAccountRepository virtualAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping
    public ClosingProcess getClosingProcess(@RequestParam Integer year, @RequestParam Integer month) {
        ClosingProcess result = closingProcessRepository.findClosingProcessByYearAndMonth(year, month);
        if (result == null) {
            ClosingProcess newClosingProcess = ClosingProcess.builder()
                                                             .year(year)
                                                             .month(month)
                                                             .manualEntryStatus(ClosingProcessStatus.NEW)
                                                             .uploadStatus(ClosingProcessStatus.NEW)
                                                             .creationDate(LocalDateTime.now())
                                                             .build();
            return closingProcessRepository.save(newClosingProcess);
        }
        return result;
    }

    @PostMapping("closeFileUpload")
    public ClosingProcess closeFileUpload(@RequestParam Integer year, @RequestParam Integer month) {
        ClosingProcess result = closingProcessRepository.findClosingProcessByYearAndMonth(year, month);
        result.setUploadStatus(ClosingProcessStatus.DONE);
        return closingProcessRepository.save(result);
    }

    @PostMapping
    public ResponseEntity<List<ScannedTransaction>> uploadFile(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam MultipartFile file) throws IOException {
        ClosingProcess closingProcess = closingProcessRepository.findClosingProcessByYearAndMonth(year, month);
        if (closingProcess.getUploadStatus().equals(ClosingProcessStatus.NEW)) {
            Reader reader = new InputStreamReader(file.getInputStream());
            List<ScannedTransaction> scannedTransactions = new CsvToBeanBuilder<ScannedTransactionBean>(reader)
                    .withType(ScannedTransactionBean.class)
                    .build()
                    .stream()
                    .filter(stb -> !stb.getDescription().contains("IHRE ZAHLUNG – BESTEN DANK"))
                    .map(stb -> stb.mapTopScannedTransaction(closingProcess))
                    .sorted(Comparator.comparing(ScannedTransaction::getDate))
                    .collect(Collectors.toList());

            closingProcess.setUploadStatus(ClosingProcessStatus.STARTED);
            closingProcessRepository.save(closingProcess);
            return ResponseEntity.ok(scannedTransactionRepository.saveAll(scannedTransactions));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
    }

    @GetMapping("/transactions")
    public List<ScannedTransaction> getTransactions(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        ClosingProcess closingProcess = closingProcessRepository.findClosingProcessByYearAndMonth(year, month);
        if (closingProcess.getUploadStatus().equals(ClosingProcessStatus.NEW)) {
            return List.of();
        }
        return scannedTransactionRepository.findAllByClosingProcessOrderByDateAsc(closingProcess);
    }


    @PostMapping("/transactions")
    public ResponseEntity<Void> saveScannedTransactions(@RequestBody SaveScannedTransactionBoundaryDto dto) {
        List<ScannedTransaction> scannedTransactions = scannedTransactionRepository.findAllById(dto.getTransactionIds());
        ClosingProcess closingProcess = scannedTransactions.stream().findFirst().orElseThrow().getClosingProcess();
        LocalDate transactionDate = LocalDate.of(closingProcess.getYear(), closingProcess.getMonth() + 1, 1);

        Optional<VirtualAccount> creditedAccount = virtualAccountRepository.findById(dto.getCreditedAccountId());
        Optional<VirtualAccount> debitedAccount = virtualAccountRepository.findById(dto.getDebitedAccountId());
        Optional<VirtualAccount> throughAccount = dto.getThroughAccountId() != null ? virtualAccountRepository.findById(
                dto.getThroughAccountId()) : Optional.empty();

        if (throughAccount.isPresent()) {
            createTransactions(scannedTransactions,
                    creditedAccount.orElseThrow(),
                    debitedAccount.orElseThrow(),
                    throughAccount.get(),
                    transactionDate);
        } else {
            createTransactions(scannedTransactions,
                    creditedAccount.orElseThrow(),
                    debitedAccount.orElseThrow(),
                    transactionDate);
        }

        return ResponseEntity.ok(null);
    }

    private void createTransactions(List<ScannedTransaction> scannedTransactions, VirtualAccount creditedAccount,
                                    VirtualAccount debitedAccount, LocalDate date) {
        List<Transaction> transactions = scannedTransactions.stream()
                                                            .map(ScannedTransaction::createTransaction)
                                                            .map(sc -> createTransaction(sc,
                                                                    creditedAccount,
                                                                    debitedAccount,
                                                                    date)).collect(Collectors.toList());

        scannedTransactionRepository.saveAll(scannedTransactions);
        transactionRepository.saveAll(transactions);
    }

    private void createTransactions(List<ScannedTransaction> scannedTransactions, VirtualAccount creditedAccount,
                                    VirtualAccount debitedAccount, VirtualAccount throughtAccount, LocalDate date) {

        List<Transaction> transactions = scannedTransactions.stream()
                                                            .map(ScannedTransaction::createTransaction)
                                                            .map(sc -> createThroughTransactions(sc,
                                                                    creditedAccount,
                                                                    debitedAccount,
                                                                    throughtAccount,
                                                                    date))
                                                            .flatMap(List::stream)
                                                            .collect(Collectors.toList());

        scannedTransactionRepository.saveAll(scannedTransactions);
        transactionRepository.saveAll(transactions);
    }

    private Transaction createTransaction(ScannedTransaction scannedTransaction, VirtualAccount creditedAccount,
                                          VirtualAccount debitedAccout, LocalDate date) {
        return Transaction.builder()
                          .date(date)
                          .description(scannedTransaction.getDescription())
                          .effectiveAmount(scannedTransaction.getAmount())
                          .indication(TransactionIndication.EXPECTED)
                          .paymentStatus(PaymentStatus.PAID)
                          .paymentType(PaymentType.DEPOSIT)
                          .creditedAccount(creditedAccount)
                          .debitedAccount(debitedAccout)
                          .budgetedAmount(BigDecimal.ZERO)
                          .build();
    }

    private List<Transaction> createThroughTransactions(ScannedTransaction scannedTransaction,
                                                        VirtualAccount creditedAccount,
                                                        VirtualAccount debitedAccount, VirtualAccount throughAccount,
                                                        LocalDate date) {
        Transaction t1 = createTransaction(scannedTransaction, creditedAccount, throughAccount, date);
        Transaction t2 = createTransaction(scannedTransaction, throughAccount, debitedAccount, date);

        return List.of(t1, t2);
    }
}
