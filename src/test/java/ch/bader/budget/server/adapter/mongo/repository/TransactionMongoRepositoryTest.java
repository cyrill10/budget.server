package ch.bader.budget.server.adapter.mongo.repository;

import ch.bader.budget.server.adapter.mongo.entity.TransactionDbo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class TransactionMongoRepositoryTest {

    @Autowired
    TransactionMongoRepository sut;

    @Test
    @DirtiesContext
    void findAllByDateBetween() {
        //arrange
        LocalDate july31st = LocalDate.of(2022, 7, 31);
        TransactionDbo lastDayOfMonthBefore = TransactionDbo.builder().date(july31st).build();

        LocalDate aug1st = LocalDate.of(2022, 8, 1);
        TransactionDbo firstDayOfMonth = TransactionDbo.builder().date(aug1st).build();

        LocalDate aug31st = LocalDate.of(2022, 8, 31);
        TransactionDbo lastDayOfMonth = TransactionDbo.builder().date(aug31st).build();

        LocalDate sep1st = LocalDate.of(2022, 9, 1);
        TransactionDbo firstDayOfMonthAfter = TransactionDbo.builder().date(sep1st).build();

        sut.saveAll(List.of(lastDayOfMonthBefore, firstDayOfMonth, lastDayOfMonth, firstDayOfMonthAfter));

        List<TransactionDbo> transations = sut.findAllByDateBetween(july31st, sep1st);
        assertThat(transations.size()).isEqualTo(2);
        assertThat(transations).contains(firstDayOfMonth);
        assertThat(transations).contains(lastDayOfMonth);
    }


    @Test
    @DirtiesContext
    void findAllByDateBetweenAndVirtualAccountId() {
        String accountIncluded = "accountIncluded";
        String accountAlsoIncluded = "accountAlsoIncluded";

        String accountExcluded = "accountExcluded";


        //arrange
        LocalDate july31st = LocalDate.of(2022, 7, 31);
        TransactionDbo lastDayOfMonthBeforeIncl = TransactionDbo
            .builder()
            .date(july31st)
            .creditedAccountId(accountIncluded)
            .debitedAccountId(accountAlsoIncluded)
            .build();

        LocalDate aug1st = LocalDate.of(2022, 8, 1);
        TransactionDbo firstDayOfMonthExcl = TransactionDbo
            .builder()
            .date(aug1st)
            .debitedAccountId(accountExcluded)
            .creditedAccountId(accountExcluded)
            .build();
        TransactionDbo firstDayOfMonthIncl = TransactionDbo
            .builder()
            .date(aug1st)
            .debitedAccountId(accountIncluded)
            .creditedAccountId(accountExcluded)
            .build();
        TransactionDbo firstDayOfMonthDoubleIncl = TransactionDbo
            .builder()
            .date(aug1st)
            .debitedAccountId(accountIncluded)
            .creditedAccountId(accountIncluded)
            .build();


        LocalDate aug31st = LocalDate.of(2022, 8, 31);
        TransactionDbo lastDayOfMonthExcl = TransactionDbo
            .builder()
            .date(aug31st)
            .debitedAccountId(accountExcluded)
            .creditedAccountId(accountExcluded)
            .build();

        TransactionDbo lastDayOfMonthIncl = TransactionDbo
            .builder()
            .date(aug31st)
            .debitedAccountId(accountExcluded)
            .creditedAccountId(accountIncluded)
            .build();

        TransactionDbo lastDayOfMonthAlsoIncl = TransactionDbo
            .builder()
            .date(aug31st)
            .debitedAccountId(accountExcluded)
            .creditedAccountId(accountAlsoIncluded)
            .build();

        TransactionDbo lastDayOfMonthDoubleIncl = TransactionDbo
            .builder()
            .date(aug31st)
            .debitedAccountId(accountIncluded)
            .creditedAccountId(accountAlsoIncluded)
            .build();

        LocalDate sep1st = LocalDate.of(2022, 9, 1);
        TransactionDbo firstDayOfMonthAfterIncl = TransactionDbo
            .builder()
            .date(sep1st)
            .creditedAccountId(accountIncluded)
            .debitedAccountId(accountAlsoIncluded)
            .build();


        sut.saveAll(List.of(
            lastDayOfMonthBeforeIncl,
            firstDayOfMonthExcl,
            firstDayOfMonthIncl,
            firstDayOfMonthDoubleIncl,
            lastDayOfMonthExcl,
            lastDayOfMonthIncl,
            lastDayOfMonthAlsoIncl,
            lastDayOfMonthDoubleIncl,
            firstDayOfMonthAfterIncl));

        List<TransactionDbo> transactions = sut.findAllByDateBetweenAndVirtualAccountId(july31st,
            sep1st,
            List.of(accountIncluded, accountAlsoIncluded));
        assertThat(transactions.size()).isEqualTo(5);
        assertThat(transactions).contains(firstDayOfMonthIncl);
        assertThat(transactions).contains(firstDayOfMonthDoubleIncl);
        assertThat(transactions).contains(lastDayOfMonthIncl);
        assertThat(transactions).contains(lastDayOfMonthAlsoIncl);
        assertThat(transactions).contains(lastDayOfMonthDoubleIncl);
    }
}