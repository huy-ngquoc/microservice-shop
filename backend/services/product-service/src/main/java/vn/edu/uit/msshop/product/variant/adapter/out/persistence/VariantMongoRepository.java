package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantMongoRepository
        extends MongoRepository<VariantDocument, UUID> {
    List<VariantDocument> findByProductId(
            final UUID productId);
}
