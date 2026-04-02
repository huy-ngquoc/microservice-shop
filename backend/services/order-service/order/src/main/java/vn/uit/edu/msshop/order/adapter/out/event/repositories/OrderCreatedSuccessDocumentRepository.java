package vn.uit.edu.msshop.order.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedSuccessDocument;

@Repository
public interface OrderCreatedSuccessDocumentRepository extends MongoRepository<OrderCreatedSuccessDocument, UUID> {
    public List<OrderCreatedSuccessDocument> findByEventStatus(String eventStatus);

    public List<OrderCreatedSuccessDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
