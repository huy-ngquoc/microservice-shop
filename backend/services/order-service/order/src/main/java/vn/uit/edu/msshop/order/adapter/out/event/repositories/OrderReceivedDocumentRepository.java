package vn.uit.edu.msshop.order.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderReceivedDocument;

@Repository
public interface OrderReceivedDocumentRepository extends MongoRepository<OrderReceivedDocument, UUID> {
     public List<OrderReceivedDocument> findByEventStatus(String eventStatus);

    public List<OrderReceivedDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
