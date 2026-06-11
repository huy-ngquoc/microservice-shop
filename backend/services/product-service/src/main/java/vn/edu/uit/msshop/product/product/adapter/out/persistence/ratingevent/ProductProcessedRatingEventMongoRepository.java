package vn.edu.uit.msshop.product.product.adapter.out.persistence.ratingevent;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

interface ProductProcessedRatingEventMongoRepository
        extends MongoRepository<ProductProcessedRatingEventDocument, UUID> {
}
