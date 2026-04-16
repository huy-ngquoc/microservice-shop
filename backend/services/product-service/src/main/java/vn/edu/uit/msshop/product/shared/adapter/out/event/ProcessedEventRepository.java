package vn.edu.uit.msshop.product.shared.adapter.out.event;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProcessedEventRepository
        extends MongoRepository<ProcessedEvent, UUID> {
}
