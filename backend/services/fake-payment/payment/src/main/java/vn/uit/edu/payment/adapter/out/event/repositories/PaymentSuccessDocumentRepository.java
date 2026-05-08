package vn.uit.edu.payment.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import vn.uit.edu.payment.adapter.out.event.documents.PaymentSuccessDocument;

@Repository
public interface PaymentSuccessDocumentRepository extends JpaRepository<PaymentSuccessDocument, UUID> {
    public List<PaymentSuccessDocument> findByEventStatus(
            String eventStatus);

    public List<PaymentSuccessDocument> findTop50ByEventStatusOrderByCreatedAtAsc(
            String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(
            String sent,
            Instant threshold);
}
