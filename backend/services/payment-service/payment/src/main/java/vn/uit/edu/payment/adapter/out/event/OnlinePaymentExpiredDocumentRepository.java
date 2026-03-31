package vn.uit.edu.payment.adapter.out.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlinePaymentExpiredDocumentRepository extends MongoRepository<OnlinePaymentExpiredDocument, UUID> {
    public List<OnlinePaymentExpiredDocument> findByEventStatus(String eventStatus);

    public List<OnlinePaymentExpiredDocument> findTop50ByStatusOrderByCreatedAtAsc(String pending);

    public void deleteByStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
