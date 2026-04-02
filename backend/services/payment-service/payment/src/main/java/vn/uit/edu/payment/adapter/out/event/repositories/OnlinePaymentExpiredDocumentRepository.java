package vn.uit.edu.payment.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.payment.adapter.out.event.documents.OnlinePaymentExpiredDocument;

@Repository
public interface OnlinePaymentExpiredDocumentRepository extends MongoRepository<OnlinePaymentExpiredDocument, UUID> {
    public List<OnlinePaymentExpiredDocument> findByEventStatus(String eventStatus);

    public List<OnlinePaymentExpiredDocument> findTop50ByStatusOrderByCreatedAtAsc(String pending);

    public void deleteByStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
