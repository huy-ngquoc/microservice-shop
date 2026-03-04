package vn.edu.uit.msshop.product.category.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMongoRepository
        extends MongoRepository<CategoryDocument, UUID> {
}
