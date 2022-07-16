package ch.bader.budget.server.adapter.mongo.repository;

import ch.bader.budget.server.adapter.mongo.entity.RealAccountDbo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RealAccountMongoRepository extends MongoRepository<RealAccountDbo, String> {

}
