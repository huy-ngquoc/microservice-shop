package vn.edu.uit.msshop.product.product.adapter.out.persistence.rating;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

interface ProductRatingMongoRepository
        extends MongoRepository<ProductRatingDocument, UUID> {
}
