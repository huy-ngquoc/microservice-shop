package vn.uit.edu.msshop.order.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.order.adapter.out.event.documents.IncreaseSoldCountEventsDocument;
@Repository
public interface IncreaseSoldCountEventDocumentRepository extends MongoRepository<IncreaseSoldCountEventsDocument, UUID> {
    public List<IncreaseSoldCountEventsDocument> findByEventStatus(String eventStatus);

    public List<IncreaseSoldCountEventsDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
