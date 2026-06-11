package vn.edu.uit.msshop.product.product.adapter.out.persistence.count;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

interface ProductSoldCountMongoRepository
        extends MongoRepository<ProductSoldCountDocument, UUID> {
}
