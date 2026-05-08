package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStockCountMongoRepository
        extends MongoRepository<ProductStockCountDocument, UUID> {
}
