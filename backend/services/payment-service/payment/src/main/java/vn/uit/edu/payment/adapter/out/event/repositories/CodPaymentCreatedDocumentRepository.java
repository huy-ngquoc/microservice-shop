package vn.uit.edu.payment.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.payment.adapter.out.event.documents.CodPaymentCreatedDocument;

@Repository

public interface CodPaymentCreatedDocumentRepository extends JpaRepository<CodPaymentCreatedDocument, UUID> {
     public List<CodPaymentCreatedDocument> findByEventStatus(String eventStatus);

    public List<CodPaymentCreatedDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
