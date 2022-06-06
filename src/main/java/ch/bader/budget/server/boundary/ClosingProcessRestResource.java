package ch.bader.budget.server.boundary;


import ch.bader.budget.server.adapter.sql.entity.ClosingProcessDboSql;
import ch.bader.budget.server.adapter.sql.entity.ScannedTransactionDboSql;
import ch.bader.budget.server.adapter.sql.repository.jpa.ClosingProcessJpaRepository;
import ch.bader.budget.server.adapter.sql.repository.jpa.ScannedTransactionJpaRepository;
import ch.bader.budget.server.adapter.sql.repository.jpa.TransactionJpaRepository;
import ch.bader.budget.server.adapter.sql.repository.jpa.VirtualAccountJpaRepository;
import ch.bader.budget.server.boundary.dto.ClosingProcessBoundaryDto;
import ch.bader.budget.server.boundary.dto.SaveScannedTransactionBoundaryDto;
import ch.bader.budget.server.boundary.dto.ScannedTransactionBoundaryDto;
import ch.bader.budget.server.core.closingProcess.ClosingProcessService;
import ch.bader.budget.server.core.transaction.TransactionService;
import ch.bader.budget.server.domain.ClosingProcess;
import ch.bader.budget.server.domain.ScannedTransaction;
import ch.bader.budget.server.mapper.ClosingProcessMapper;
import ch.bader.budget.server.mapper.ScannedTransactionMapper;
import ch.bader.budget.server.type.ClosingProcessStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/budget/closingProcess")
public class ClosingProcessRestResource {

    @Autowired
    ClosingProcessJpaRepository closingProcessRepository;

    @Autowired
    ScannedTransactionJpaRepository scannedTransactionRepository;

    @Autowired
    VirtualAccountJpaRepository virtualAccountRepository;

    @Autowired
    TransactionJpaRepository transactionRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    ClosingProcessService closingProcessService;

    @Autowired
    ClosingProcessMapper closingProcessMapper;

    @Autowired
    ScannedTransactionMapper scannedTransactionMapper;

    @GetMapping
    public ClosingProcessBoundaryDto getClosingProcess(@RequestParam Integer year, @RequestParam Integer month) {
        ClosingProcess closingProcess = closingProcessService.getClosingProcess(year, month);
        return closingProcessMapper.mapToDto(closingProcess);
    }

    @PostMapping("closeFileUpload")
    public ClosingProcessBoundaryDto closeFileUpload(@RequestParam Integer year, @RequestParam Integer month) {
        ClosingProcess closingProcess = closingProcessService.closeFileUpload(year, month);
        return closingProcessMapper.mapToDto(closingProcess);
    }

    @PostMapping
    public ResponseEntity<List<ScannedTransactionBoundaryDto>> uploadFile(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam MultipartFile file) throws IOException {
        List<ScannedTransaction> scannedTransactions = closingProcessService.uploadFile(year, month, file);
        if (scannedTransactions != null) {
            return ResponseEntity.ok(scannedTransactions.stream().map(scannedTransactionMapper::mapToDto).collect(
                    Collectors.toList()));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).
                             body(null);

    }

    @GetMapping("/transactions")
    public List<ScannedTransactionDboSql> getTransactions(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        ClosingProcessDboSql closingProcess = closingProcessRepository.findClosingProcessByYearAndMonth(year, month);
        if (closingProcess.getUploadStatus().equals(ClosingProcessStatus.NEW)) {
            return List.of();
        }
        return scannedTransactionRepository.findAllByClosingProcessOrderByDateAsc(closingProcess);
    }


    @PostMapping("/transactions")
    public ResponseEntity<Void> saveScannedTransactions(@RequestBody SaveScannedTransactionBoundaryDto dto) {
        closingProcessService.saveScannedTransactions(dto);
        return ResponseEntity.ok(null);
    }


}
