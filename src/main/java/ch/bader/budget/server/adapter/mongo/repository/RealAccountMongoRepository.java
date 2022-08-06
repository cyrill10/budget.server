package ch.bader.budget.server.adapter.mongo.repository;

import ch.bader.budget.server.adapter.mongo.entity.RealAccountDbo;
import ch.bader.budget.server.adapter.mongo.entity.ValueEnumDbo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RealAccountMongoRepository extends MongoRepository<RealAccountDbo, String> {

    List<RealAccountDbo> findAllByAccountType(ValueEnumDbo accountType);
}
