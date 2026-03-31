package vn.uit.edu.payment.adapter.out.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlinePaymentCancelledDocumentRepository extends MongoRepository<OnlinePaymentCancelledDocument, UUID> {
    public List<OnlinePaymentCancelledDocument> findByEventStatus(String eventStatus);

    public List<OnlinePaymentCancelledDocument> findTop50ByStatusOrderByCreatedAtAsc(String pending);

    public void deleteByStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
