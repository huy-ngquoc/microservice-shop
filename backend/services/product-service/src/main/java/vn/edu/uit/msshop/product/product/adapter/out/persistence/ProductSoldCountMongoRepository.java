package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductSoldCountMongoRepository
        extends MongoRepository<ProductSoldCountDocument, UUID> {
}
