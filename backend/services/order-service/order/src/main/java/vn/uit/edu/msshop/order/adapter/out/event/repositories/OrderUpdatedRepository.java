package vn.uit.edu.msshop.order.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderUpdatedEventDocument;

@Repository
public interface OrderUpdatedRepository extends MongoRepository<OrderUpdatedEventDocument, UUID> {
    public List<OrderUpdatedEventDocument> findByEventStatus(String eventStatus);

    public List<OrderUpdatedEventDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
