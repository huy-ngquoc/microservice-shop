package vn.uit.edu.msshop.order.adapter.out.event.inventory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderShippedDocumentRepository extends MongoRepository<OrderShippedDocument, UUID> {
    public List<OrderShippedDocument> findByEventStatus(String eventStatus);

    public List<OrderShippedDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);

}
