package ch.bader.budget.server.repository;

import ch.bader.budget.server.domain.ClosingProcess;

import java.time.YearMonth;

public interface ClosingProcessAdapter {
    ClosingProcess getClosingProcess(YearMonth yearMonth);

    ClosingProcess save(ClosingProcess closingProcess);
}
