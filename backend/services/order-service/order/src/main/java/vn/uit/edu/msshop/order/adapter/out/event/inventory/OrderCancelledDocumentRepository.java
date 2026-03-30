package vn.uit.edu.msshop.order.adapter.out.event.inventory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCancelledDocumentRepository extends MongoRepository<OrderCancelledDocument, UUID>{
    public List<OrderCancelledDocument> findByEventStatus(String eventStatus);

    public List<OrderCancelledDocument> findTop50ByStatusOrderByCreatedAtAsc(String pending);

    public void deleteByStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
