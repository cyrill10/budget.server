package ch.bader.budget.server.adapter.mongo.repository;

import ch.bader.budget.server.adapter.mongo.entity.ScannedTransactionDbo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.YearMonth;
import java.util.List;

public interface ScannedTransactionMongoRepository extends MongoRepository<ScannedTransactionDbo, String> {

    List<ScannedTransactionDbo> findAllByYearMonth(YearMonth yearMonth);

    List<ScannedTransactionDbo> findAllByIdIn(List<String> ids);

}
