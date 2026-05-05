package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRatingMongoRepository extends MongoRepository<ProductRatingDocument, UUID> {
}
