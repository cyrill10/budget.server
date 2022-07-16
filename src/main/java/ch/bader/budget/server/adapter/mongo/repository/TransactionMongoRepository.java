package ch.bader.budget.server.adapter.mongo.repository;

import ch.bader.budget.server.adapter.mongo.entity.TransactionDbo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransactionMongoRepository extends MongoRepository<TransactionDbo, String> {

    List<TransactionDbo> findAllByDateBetween(LocalDate fromExclusive, LocalDate toExclusive);


    @Query("{$and :[{ 'date' : { $gt: ?0, $lt: ?1 }},{$or :[{creditedAccountId: { $in : ?2}},{debitedAccountId: { $in : ?2}}]}]}")
    List<TransactionDbo> findAllByDateBetweenAndVirtualAccountId(LocalDate fromExclusive, LocalDate toExclusive,
                                                                 List<String> virtualAccountIds);
}
