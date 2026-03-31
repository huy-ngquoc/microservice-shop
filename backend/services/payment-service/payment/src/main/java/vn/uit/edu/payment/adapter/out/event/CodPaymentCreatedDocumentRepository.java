package vn.uit.edu.payment.adapter.out.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CodPaymentCreatedDocumentRepository extends MongoRepository<CodPaymentCreatedDocument, UUID> {
     public List<CodPaymentCreatedDocument> findByEventStatus(String eventStatus);

    public List<CodPaymentCreatedDocument> findTop50ByStatusOrderByCreatedAtAsc(String pending);

    public void deleteByStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
