package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VariantSoldCountMongoRepository
    extends MongoRepository<VariantSoldCountDocument, UUID> {
}
