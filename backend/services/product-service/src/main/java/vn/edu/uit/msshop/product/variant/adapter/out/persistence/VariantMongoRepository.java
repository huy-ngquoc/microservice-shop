package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantMongoRepository
        extends MongoRepository<VariantDocument, UUID> {
    List<VariantDocument> findAllByIdAndDeletionTimeIsNull(
            final Iterable<UUID> ids);

    Optional<VariantDocument> findByIdAndDeletionTimeIsNull(
            final UUID id);

    Optional<VariantDocument> findByIdAndDeletionTimeIsNotNull(
            final UUID id);

    List<VariantDocument> findAllByProductId(
            final UUID productId);

    void deleteAllByProductId(
            final UUID productId);
}
