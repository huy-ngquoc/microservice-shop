package vn.uit.edu.payment.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentCancelledDocument;

@Repository
public interface OnlinePaymentCancelledDocumentRepository extends MongoRepository<OnlinePaymentCancelledDocument, UUID> {
    public List<OnlinePaymentCancelledDocument> findByEventStatus(String eventStatus);

    public List<OnlinePaymentCancelledDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
