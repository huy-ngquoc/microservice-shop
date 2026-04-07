package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMongoRepository
        extends MongoRepository<ProductDocument, UUID> {
    Optional<ProductDocument> findByIdAndDeletionTimeIsNull(
            final UUID id);

    Optional<ProductDocument> findByIdAndDeletionTimeIsNotNull(
            final UUID id);

    // TODO: "existsByBrandIdAndDeletionTimeIsNull"
    boolean existsByBrandId(
            final UUID brandId);

    boolean existsByCategoryId(
            final UUID categoryId);
}
