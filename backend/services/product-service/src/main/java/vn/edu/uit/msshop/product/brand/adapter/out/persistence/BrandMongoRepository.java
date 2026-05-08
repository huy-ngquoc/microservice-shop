package vn.edu.uit.msshop.product.brand.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandMongoRepository extends MongoRepository<BrandDocument, UUID> {
    Page<BrandDocument> findAllByDeletionTimeIsNull(
            final Pageable pageable);

    Page<BrandDocument> findAllByDeletionTimeIsNotNull(
            final Pageable pageable);

    Optional<BrandDocument> findByIdAndDeletionTimeIsNull(
            final UUID id);

    Optional<BrandDocument> findByIdAndDeletionTimeIsNotNull(
            final UUID id);

    boolean existsByIdAndDeletionTimeIsNull(
            final UUID id);
}
