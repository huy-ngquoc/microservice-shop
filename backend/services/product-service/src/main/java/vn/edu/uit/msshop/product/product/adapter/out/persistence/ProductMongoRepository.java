package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMongoRepository
        extends MongoRepository<ProductDocument, UUID> {
    Page<ProductDocument> findAllByDeletionTimeIsNull(
            final Pageable pageable);

    Page<ProductDocument> findAllByDeletionTimeIsNotNull(
            final Pageable pageable);

    Optional<ProductDocument> findByIdAndDeletionTimeIsNull(
            final UUID id);

    Optional<ProductDocument> findByIdAndDeletionTimeIsNotNull(
            final UUID id);

    boolean existsByBrandIdAndDeletionTimeIsNull(
            final UUID brandId);

    boolean existsByBrandIdAndDeletionTimeIsNotNull(
            final UUID brandId);

    boolean existsByCategoryIdAndDeletionTimeIsNull(
            final UUID categoryId);

    boolean existsByCategoryIdAndDeletionTimeIsNotNull(
            final UUID categoryId);

    boolean existsByVariants_Id(
            final UUID variantId);
}
