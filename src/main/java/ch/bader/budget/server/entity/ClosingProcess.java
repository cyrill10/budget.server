package ch.bader.budget.server.entity;

import ch.bader.budget.server.type.ClosingProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueYearAndMonth", columnNames = {"year", "month"})})
public class ClosingProcess {

    @Id
    @SequenceGenerator(name = "closing_process_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "closing_process_seq")
    private Integer id;

    private int year;

    private int month;

    @PreUpdate
    void fillUpdate() {
        updateDate = LocalDateTime.now();
    }

    private ClosingProcessStatus uploadStatus;

    private ClosingProcessStatus manualEntryStatus;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;
}
