package vn.uit.edu.msshop.order.adapter.out.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCreatedDocumentRepository extends MongoRepository<OrderCreatedDocument, UUID> {
    public List<OrderCreatedDocument> findByEventStatus(String eventStatus);

    public List<OrderCreatedDocument> findTop50ByStatusOrderByCreatedAtAsc(String pending);

    public void deleteByStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
