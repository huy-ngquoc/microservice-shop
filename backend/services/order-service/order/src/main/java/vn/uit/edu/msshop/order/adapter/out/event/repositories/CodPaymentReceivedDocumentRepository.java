package vn.uit.edu.msshop.order.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.order.adapter.out.event.documents.CodPaymentReceivedDocument;

@Repository
public interface CodPaymentReceivedDocumentRepository extends MongoRepository<CodPaymentReceivedDocument, UUID> {
    public List<CodPaymentReceivedDocument> findByEventStatus(String eventStatus);

    public List<CodPaymentReceivedDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
