package ch.bader.budget.server.mapper;

import ch.bader.budget.server.adapter.mongo.entity.ClosingProcessDbo;
import ch.bader.budget.server.adapter.mongo.entity.ValueEnumDbo;
import ch.bader.budget.server.boundary.dto.ClosingProcessBoundaryDto;
import ch.bader.budget.server.domain.ClosingProcess;
import ch.bader.budget.server.type.ClosingProcessStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ClosingProcessMapperTest {

    @Autowired
    private ClosingProcessMapperImpl sut;

    @Test
    public void shouldMapClosingProcessToDto() {
        //given
        ClosingProcess domain = ClosingProcess.builder()
                                              .id("id")
                                              .yearMonth(YearMonth.of(2022, 1))
                                              .manualEntryStatus(ClosingProcessStatus.NEW)
                                              .uploadStatus(ClosingProcessStatus.DONE)
                                              .build();

        //when
        ClosingProcessBoundaryDto dto = sut.mapToDto(domain);

        //then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("id");
        assertThat(dto.getYear()).isEqualTo(2022);
        assertThat(dto.getMonth()).isEqualTo(0);
        assertThat(dto.getManualEntryStatus().getValue()).isEqualTo(ClosingProcessStatus.NEW.getValue());
        assertThat(dto.getUploadStatus().getValue()).isEqualTo(ClosingProcessStatus.DONE.getValue());
    }

    @Test
    public void shouldMapClosingProcessToDbo() {
        //given
        ClosingProcess domain = ClosingProcess.builder()
                                              .id("5")
                                              .yearMonth(YearMonth.of(2022, 1))
                                              .manualEntryStatus(ClosingProcessStatus.NEW)
                                              .uploadStatus(ClosingProcessStatus.DONE)
                                              .build();

        //when
        ClosingProcessDbo dbo = sut.mapToEntity(domain);

        //then
        assertThat(dbo).isNotNull();
        assertThat(dbo.getId()).isEqualTo("5");
        assertThat(dbo.getYearMonth()).isEqualTo(YearMonth.of(2022, 1));
        assertThat(dbo.getManualEntryStatus().getValue()).isEqualTo(ClosingProcessStatus.NEW.getValue());
        assertThat(dbo.getUploadStatus().getValue()).isEqualTo(ClosingProcessStatus.DONE.getValue());
    }

    @Test
    public void shouldMapDboToClosingProcess() {
        //given
        ClosingProcessDbo dbo = ClosingProcessDbo.builder()
                                                 .id("5")
                                                 .yearMonth(YearMonth.of(2022, 1))
                                                 .manualEntryStatus(ValueEnumDbo
                                                     .builder()
                                                     .value(ClosingProcessStatus.DONE.getValue())
                                                     .name(ClosingProcessStatus.DONE.getName())
                                                     .build())
                                                 .uploadStatus(ValueEnumDbo
                                                     .builder()
                                                     .value(ClosingProcessStatus.STARTED.getValue())
                                                     .name(ClosingProcessStatus.STARTED.getName())
                                                     .build())
                                                 .build();
        //when
        ClosingProcess domain = sut.mapToDomain(dbo);

        //then
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo("5");
        assertThat(domain.getYearMonth()).isEqualTo(YearMonth.of(2022, 1));
        assertThat(domain.getManualEntryStatus()).isEqualTo(ClosingProcessStatus.DONE);
        assertThat(domain.getUploadStatus()).isEqualTo(ClosingProcessStatus.STARTED);
    }
}