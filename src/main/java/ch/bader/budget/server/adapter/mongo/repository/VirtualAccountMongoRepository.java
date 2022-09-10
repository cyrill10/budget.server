package ch.bader.budget.server.adapter.mongo.repository;

import ch.bader.budget.server.adapter.mongo.entity.VirtualAccountDbo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

@SuppressWarnings("unused")
public interface VirtualAccountMongoRepository extends MongoRepository<VirtualAccountDbo, String> {

    List<VirtualAccountDbo> findAllByUnderlyingAccountId(String id);

}
