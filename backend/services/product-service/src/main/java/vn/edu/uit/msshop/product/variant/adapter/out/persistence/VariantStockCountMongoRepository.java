package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VariantStockCountMongoRepository
        extends MongoRepository<VariantStockCountDocument, UUID> {
}
