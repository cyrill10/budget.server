package ch.bader.budget.server.core.closingProcess;

import ch.bader.budget.server.boundary.dto.SaveScannedTransactionBoundaryDto;
import ch.bader.budget.server.domain.ClosingProcess;
import ch.bader.budget.server.domain.ScannedTransaction;
import ch.bader.budget.server.domain.Transaction;
import ch.bader.budget.server.domain.VirtualAccount;
import ch.bader.budget.server.process.closing.ScannedTransactionBean;
import ch.bader.budget.server.repository.ClosingProcessRepository;
import ch.bader.budget.server.repository.ScannedTransactionRepository;
import ch.bader.budget.server.repository.TransactionAdapter;
import ch.bader.budget.server.repository.VirtualAccountAdapter;
import ch.bader.budget.server.type.ClosingProcessStatus;
import ch.bader.budget.server.type.PaymentStatus;
import ch.bader.budget.server.type.PaymentType;
import ch.bader.budget.server.type.TransactionIndication;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClosingProcessService {

    @Autowired
    ClosingProcessRepository closingProcessRepository;

    @Autowired
    ScannedTransactionRepository scannedTransactionRepository;

    @Autowired
    @Qualifier("virtualAccountMySql")
    private VirtualAccountAdapter virtualAccountRepository;

    @Autowired
    @Qualifier("transactionMySql")
    TransactionAdapter transactionRepository;

    public ClosingProcess getClosingProcess(Integer year, Integer month) {
        return closingProcessRepository.getClosingProcess(year, month);
    }

    public ClosingProcess closeFileUpload(@RequestParam Integer year, @RequestParam Integer month) {
        ClosingProcess result = closingProcessRepository.getClosingProcess(year, month);
        result.setUploadStatus(ClosingProcessStatus.DONE);
        return closingProcessRepository.save(result);
    }

    public List<ScannedTransaction> uploadFile(
        Integer year,
        Integer month,
        MultipartFile file) throws IOException {
        ClosingProcess closingProcess = closingProcessRepository.getClosingProcess(year, month);
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
            return scannedTransactionRepository.saveAll(scannedTransactions);
        }
        return null;
    }

    public List<ScannedTransaction> getTransactions(
        Integer year,
        Integer month) {
        return scannedTransactionRepository.getTransactionsForClosingProcess(year,
            month);
    }


    public void saveScannedTransactions(SaveScannedTransactionBoundaryDto dto) {
        List<ScannedTransaction> scannedTransactions = scannedTransactionRepository.findAllById(dto.getTransactionIds());
        ClosingProcess closingProcess = scannedTransactions.stream()
                                                           .findFirst()
                                                           .orElseThrow()
                                                           .getClosingProcess();
        LocalDate transactionDate = LocalDate.of(closingProcess.getYear(), closingProcess.getMonth() + 1, 10);

        VirtualAccount creditedAccount = virtualAccountRepository.getAccountById(dto.getCreditedAccountId());
        VirtualAccount debitedAccount = virtualAccountRepository.getAccountById(dto.getDebitedAccountId());
        VirtualAccount throughAccount = null;

        if (dto.getThroughAccountId() != null) {
            throughAccount = virtualAccountRepository.getAccountById(
                dto.getThroughAccountId());
        }

        List<Transaction> transactions;
        if (throughAccount != null) {
            transactions = createTransactions(scannedTransactions,
                creditedAccount,
                debitedAccount,
                throughAccount,
                transactionDate);
        } else {
            transactions = createTransactions(scannedTransactions,
                creditedAccount,
                debitedAccount,
                transactionDate);
        }
        scannedTransactionRepository.saveAll(scannedTransactions);
        transactionRepository.saveTransactions(transactions);
    }

    private List<Transaction> createTransactions(List<ScannedTransaction> scannedTransactions,
                                                 VirtualAccount creditedAccount,
                                                 VirtualAccount debitedAccount, LocalDate date) {
        return scannedTransactions.stream()
                                  .map(ScannedTransaction::createTransaction)
                                  .map(sc -> createTransaction(sc,
                                      creditedAccount,
                                      debitedAccount,
                                      date)).collect(Collectors.toList());


    }

    private List<Transaction> createTransactions(List<ScannedTransaction> scannedTransactions,
                                                 VirtualAccount creditedAccount,
                                                 VirtualAccount debitedAccount, VirtualAccount throughtAccount,
                                                 LocalDate date) {

        return scannedTransactions.stream()
                                  .map(ScannedTransaction::createTransaction)
                                  .map(sc -> createThroughTransactions(sc,
                                      creditedAccount,
                                      debitedAccount,
                                      throughtAccount,
                                      date))
                                  .flatMap(List::stream)
                                  .collect(Collectors.toList());
    }

    private Transaction createTransaction(ScannedTransaction scannedTransaction,
                                          VirtualAccount creditedAccount,
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
                                                        VirtualAccount debitedAccount,
                                                        VirtualAccount throughAccount,
                                                        LocalDate date) {
        Transaction t1 = createTransaction(scannedTransaction, creditedAccount, throughAccount, date);
        Transaction t2 = createTransaction(scannedTransaction, throughAccount, debitedAccount, date);

        return List.of(t1, t2);
    }
}
