package vn.edu.uit.msshop.product.product.adapter.out.persistence.ratingevent;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ProcessedRatingEventMongoRepository
        extends MongoRepository<ProcessedRatingEventDocument, UUID> {
}
