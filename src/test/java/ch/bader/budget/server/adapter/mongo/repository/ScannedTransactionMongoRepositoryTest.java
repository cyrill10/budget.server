package ch.bader.budget.server.adapter.mongo.repository;

import ch.bader.budget.server.adapter.mongo.entity.ScannedTransactionDbo;
import ch.bader.budget.server.converter.Converters;
import ch.bader.budget.server.converter.DataMongoTypeExcludeFilterWithConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@OverrideAutoConfiguration(enabled = true)
@TypeExcludeFilters(DataMongoTypeExcludeFilterWithConverter.class)
@Import(Converters.class)
class ScannedTransactionMongoRepositoryTest {

    @Autowired
    ScannedTransactionMongoRepository sut;

    @Test
    @DirtiesContext
    void findAllByYearMonth() {

        //arrange
        ScannedTransactionDbo findableDbo = ScannedTransactionDbo
            .builder()
            .yearMonth(YearMonth.of(2021, 1))
            .description("findableDbo")
            .build();

        ScannedTransactionDbo alsoFindableDbo = ScannedTransactionDbo
            .builder()
            .yearMonth(YearMonth.of(2021, 1))
            .description("alsoFindableDbo")
            .build();

        ScannedTransactionDbo notFindableDbo = ScannedTransactionDbo
            .builder()
            .yearMonth(YearMonth.of(2021, 2))
            .description("notFindableDbo")
            .build();

        ScannedTransactionDbo alsoNotFindableDbo = ScannedTransactionDbo
            .builder()
            .yearMonth(YearMonth.of(2022, 1))
            .description("alsoNotFindableDbo")
            .build();

        sut.saveAll(List.of(findableDbo, notFindableDbo, alsoNotFindableDbo, alsoFindableDbo));

        //act
        List<ScannedTransactionDbo> results = sut.findAllByYearMonth(YearMonth.of(2021, 1));

        assertThat(results).hasSize(2);
        assertThat(results).containsAll(List.of(findableDbo, alsoFindableDbo));

    }
}