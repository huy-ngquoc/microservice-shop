package vn.edu.uit.msshop.product.product.adapter.out.persistence.count;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ProductStockCountMongoRepository
        extends MongoRepository<ProductStockCountDocument, UUID> {
}
