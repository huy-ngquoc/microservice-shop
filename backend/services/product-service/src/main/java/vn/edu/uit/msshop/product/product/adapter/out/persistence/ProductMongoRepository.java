package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMongoRepository
        extends MongoRepository<ProductDocument, UUID> {
    // TODO: "existsByBrandIdAndDeletionTimeIsNull"
    boolean existsByBrandId(
            final UUID brandId);

    boolean existsByCategoryId(
            final UUID categoryId);
}
