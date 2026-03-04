package vn.edu.uit.msshop.product.brand.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandMongoRepository
        extends MongoRepository<BrandDocument, UUID> {
}
