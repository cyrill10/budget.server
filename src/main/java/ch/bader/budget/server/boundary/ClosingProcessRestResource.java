package ch.bader.budget.server.boundary;


import ch.bader.budget.server.boundary.dto.ClosingProcessBoundaryDto;
import ch.bader.budget.server.boundary.dto.SaveScannedTransactionBoundaryDto;
import ch.bader.budget.server.boundary.dto.ScannedTransactionBoundaryDto;
import ch.bader.budget.server.boundary.dto.TransferDetailDto;
import ch.bader.budget.server.core.closingProcess.ClosingProcessService;
import ch.bader.budget.server.domain.ClosingProcess;
import ch.bader.budget.server.domain.ScannedTransaction;
import ch.bader.budget.server.mapper.ClosingProcessMapper;
import ch.bader.budget.server.mapper.ScannedTransactionMapper;
import ch.bader.budget.server.mapper.TransferDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/budget/closingProcess")
public class ClosingProcessRestResource {


    @Autowired
    ClosingProcessService closingProcessService;

    @Autowired
    ClosingProcessMapper closingProcessMapper;

    @Autowired
    ScannedTransactionMapper scannedTransactionMapper;

    @Autowired
    TransferDetailMapper transferDetailsMapper;

    @GetMapping
    public ClosingProcessBoundaryDto getClosingProcess(@RequestParam Integer year, @RequestParam Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month + 1);
        ClosingProcess closingProcess = closingProcessService.getClosingProcess(yearMonth);
        return closingProcessMapper.mapToDto(closingProcess);
    }

    @PostMapping("closeFileUpload")
    public ClosingProcessBoundaryDto closeFileUpload(@RequestParam Integer year, @RequestParam Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month + 1);
        ClosingProcess closingProcess = closingProcessService.closeFileUpload(yearMonth);
        return closingProcessMapper.mapToDto(closingProcess);
    }

    @PostMapping
    public ResponseEntity<List<ScannedTransactionBoundaryDto>> uploadFile(
        @RequestParam Integer year,
        @RequestParam Integer month,
        @RequestParam MultipartFile file) throws IOException {
        YearMonth yearMonth = YearMonth.of(year, month + 1);
        List<ScannedTransaction> scannedTransactions = closingProcessService.uploadFile(yearMonth, file);
        if (scannedTransactions != null) {
            return ResponseEntity.ok(scannedTransactions.stream().map(scannedTransactionMapper::mapToDto).collect(
                Collectors.toList()));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).
                             body(null);

    }

    @GetMapping("/transactions")
    public List<ScannedTransactionBoundaryDto> getTransactions(
        @RequestParam Integer year,
        @RequestParam Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month + 1);
        return closingProcessService
            .getTransactions(yearMonth)
            .stream()
            .map(scannedTransactionMapper::mapToDto)
            .collect(Collectors.toList());
    }


    @PostMapping("/transactions")
    public ResponseEntity<Void> saveScannedTransactions(@RequestBody SaveScannedTransactionBoundaryDto dto) {
        closingProcessService.saveScannedTransactions(dto);
        return ResponseEntity.ok(null);
    }


    @GetMapping("/transfer/details")
    public List<TransferDetailDto> getTransferDetails(@RequestParam Integer year,
                                                      @RequestParam Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month + 1);
        return closingProcessService
            .getTransferDetails(yearMonth)
            .stream()
            .map(transferDetailsMapper::mapToDto).collect(Collectors.toList());
    }

    @PostMapping("/transfer/close")
    public ClosingProcessBoundaryDto closeTransfer(@RequestParam Integer year, @RequestParam Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month + 1);
        ClosingProcess closingProcess = closingProcessService.closeTransfer(yearMonth);
        return closingProcessMapper.mapToDto(closingProcess);
    }
}
