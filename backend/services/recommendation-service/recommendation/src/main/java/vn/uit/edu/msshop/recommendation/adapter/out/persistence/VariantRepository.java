package vn.uit.edu.msshop.recommendation.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface VariantRepository extends MongoRepository<VariantMongoDbDocument, UUID> {

}
