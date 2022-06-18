package ch.bader.budget.server.adapter.mongo.repository;

import ch.bader.budget.server.adapter.mongo.entity.ClosingProcessDbo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.YearMonth;

public interface ClosingProcessMongoRepository extends MongoRepository<ClosingProcessDbo, String> {

    ClosingProcessDbo findByYearMonth(YearMonth yearMonth);

}
