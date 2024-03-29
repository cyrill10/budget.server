package ch.bader.budget.server.core.process.closing;

import ch.bader.budget.server.boundary.dto.SaveScannedTransactionBoundaryDto;
import ch.bader.budget.server.core.transaction.TransactionService;
import ch.bader.budget.server.domain.*;
import ch.bader.budget.server.process.closing.ScannedTransactionCsvBean;
import ch.bader.budget.server.repository.*;
import ch.bader.budget.server.type.*;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClosingProcessService {

    private final ClosingProcessAdapter closingProcessAdapter;

    private final ScannedTransactionAdapter scannedTransactionAdapter;

    private final VirtualAccountAdapter virtualAccountAdapter;

    private final TransactionAdapter transactionAdapter;

    private final RealAccountAdapter realAccountAdapter;

    private final TransactionService transactionService;

    public ClosingProcessService(ClosingProcessAdapter closingProcessAdapter,
                                 ScannedTransactionAdapter scannedTransactionAdapter,
                                 VirtualAccountAdapter virtualAccountAdapter,
                                 TransactionAdapter transactionAdapter,
                                 RealAccountAdapter realAccountAdapter,
                                 TransactionService transactionService) {
        this.closingProcessAdapter = closingProcessAdapter;
        this.scannedTransactionAdapter = scannedTransactionAdapter;
        this.virtualAccountAdapter = virtualAccountAdapter;
        this.transactionAdapter = transactionAdapter;
        this.realAccountAdapter = realAccountAdapter;
        this.transactionService = transactionService;
    }

    public ClosingProcess getClosingProcess(YearMonth yearMonth) {
        return closingProcessAdapter.getClosingProcess(yearMonth);
    }

    public ClosingProcess closeFileUpload(YearMonth yearMonth) {
        ClosingProcess result = closingProcessAdapter.getClosingProcess(yearMonth);
        result.setUploadStatus(ClosingProcessStatus.DONE);
        return closingProcessAdapter.save(result);
    }

    public ClosingProcess closeTransfer(YearMonth yearMonth) {
        ClosingProcess result = closingProcessAdapter.getClosingProcess(yearMonth);
        result.setTransferStatus(ClosingProcessStatus.DONE);
        return closingProcessAdapter.save(result);
    }

    public List<TransferDetails> getTransferDetails(YearMonth yearMonth) {
        List<RealAccount> realAccounts = realAccountAdapter.getAccountsByTyp(AccountType.SAVING);
        return realAccounts.stream().map(r -> {
                    List<TransactionListElement> transactionListElements = transactionService.getAllTransactionsForMonthAndRealAccount(
                            yearMonth.atDay(1), r.getId());
                    return extractTransferDetails(r.getName(), transactionListElements);
                }
        ).sorted().collect(Collectors.toList());
    }

    private TransferDetails extractTransferDetails(String accountName, List<TransactionListElement> transactionListElements) {
        TransactionListElement balanceBefore = transactionListElements.get(0);
        TransactionListElement balanceAfter = transactionListElements.get(transactionListElements.size() - 1);

        return TransferDetails
                .builder()
                .transferAmount(balanceAfter.getEffectiveBalance().subtract(balanceBefore.getEffectiveBalance()))
                .accountName(accountName)
                .build();
    }

    public List<ScannedTransaction> uploadFile(YearMonth yearMonth, MultipartFile file) throws IOException {
        ClosingProcess closingProcess = closingProcessAdapter.getClosingProcess(yearMonth);
        if (closingProcess.getUploadStatus().equals(ClosingProcessStatus.NEW)) {
            Reader reader = new InputStreamReader(file.getInputStream());
            List<ScannedTransaction> scannedTransactions = new CsvToBeanBuilder<ScannedTransactionCsvBean>(reader)
                    .withType(ScannedTransactionCsvBean.class)
                    .build()
                    .stream()
                    .filter(stb -> !stb.getDescription().contains("IHRE ZAHLUNG – BESTEN DANK"))
                    .map(stb -> stb.mapTopScannedTransaction(closingProcess))
                    .sorted()
                    .collect(Collectors.toList());

            closingProcess.setUploadStatus(ClosingProcessStatus.STARTED);
            closingProcessAdapter.save(closingProcess);
            return scannedTransactionAdapter.saveAll(scannedTransactions);
        }
        return List.of();
    }

    public List<ScannedTransaction> getTransactions(YearMonth yearMonth) {
        return scannedTransactionAdapter.getTransactionsForYearMonth(yearMonth);
    }


    public void saveScannedTransactions(SaveScannedTransactionBoundaryDto dto) {
        List<ScannedTransaction> scannedTransactions = scannedTransactionAdapter.findAllById(dto.getTransactionIds());
        YearMonth yearMonth = scannedTransactions.stream()
                .findFirst()
                .orElseThrow()
                .getYearMonth();

        LocalDate transactionDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 10);

        VirtualAccount creditedAccount = virtualAccountAdapter.getAccountById(dto.getCreditedAccountId());
        VirtualAccount debitedAccount = virtualAccountAdapter.getAccountById(dto.getDebitedAccountId());
        VirtualAccount throughAccount = null;

        if (dto.getThroughAccountId() != null) {
            throughAccount = virtualAccountAdapter.getAccountById(
                    dto.getThroughAccountId());
        }

        List<Transaction> transactions = createTransactions(scannedTransactions, transactionDate, creditedAccount, debitedAccount, throughAccount);

        scannedTransactionAdapter.saveAll(scannedTransactions);
        transactionAdapter.saveTransactions(transactions);
    }

    private List<Transaction> createTransactions(List<ScannedTransaction> scannedTransactions, LocalDate transactionDate, VirtualAccount creditedAccount, VirtualAccount debitedAccount, VirtualAccount throughAccount) {
        List<Transaction> transactions;
        if (throughAccount != null) {
            transactions = createTransactionsWithThroughAccount(scannedTransactions,
                    creditedAccount,
                    debitedAccount,
                    throughAccount,
                    transactionDate);
        } else {
            transactions = createTransactionsWithoutThroughAccount(scannedTransactions,
                    creditedAccount,
                    debitedAccount,
                    transactionDate);
        }
        return transactions;
    }

    private List<Transaction> createTransactionsWithoutThroughAccount(List<ScannedTransaction> scannedTransactions,
                                                                      VirtualAccount creditedAccount,
                                                                      VirtualAccount debitedAccount, LocalDate date) {
        return scannedTransactions.stream()
                .sorted()
                .map(ScannedTransaction::createTransaction)
                .map(sc -> createTransaction(sc,
                        creditedAccount,
                        debitedAccount,
                        date)).collect(Collectors.toList());


    }

    private List<Transaction> createTransactionsWithThroughAccount(List<ScannedTransaction> scannedTransactions,
                                                                   VirtualAccount creditedAccount,
                                                                   VirtualAccount debitedAccount, VirtualAccount throughAccount,
                                                                   LocalDate date) {

        return scannedTransactions.stream()
                .sorted()
                .map(ScannedTransaction::createTransaction)
                .map(sc -> createThroughTransactions(sc,
                        creditedAccount,
                        debitedAccount,
                        throughAccount,
                        date))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private Transaction createTransaction(ScannedTransaction scannedTransaction,
                                          VirtualAccount creditedAccount,
                                          VirtualAccount debitedAccount, LocalDate date) {
        return Transaction.builder()
                .date(date)
                .description(scannedTransaction.getDescription())
                .effectiveAmount(scannedTransaction.getAmount())
                .indication(TransactionIndication.EXPECTED)
                .paymentStatus(PaymentStatus.PAID)
                .paymentType(PaymentType.DEPOSIT)
                .creditedAccount(creditedAccount)
                .debitedAccount(debitedAccount)
                .budgetedAmount(BigDecimal.ZERO)
                .creationDate(LocalDateTime.now())
                .build();
    }

    private List<Transaction> createThroughTransactions(ScannedTransaction scannedTransaction,
                                                        VirtualAccount creditedAccount,
                                                        VirtualAccount debitedAccount,
                                                        VirtualAccount throughAccount,
                                                        LocalDate date) {
        Transaction t1 = createTransaction(scannedTransaction, creditedAccount, throughAccount, date);
        Transaction t2 = createTransaction(scannedTransaction, throughAccount, debitedAccount, date);

        return List.of(t1, t2);
    }
}
