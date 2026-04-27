package vn.uit.edu.msshop.inventory.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.inventory.adapter.out.event.documents.ForceCancellOrderDocument;
@Repository
public interface ForceCancellOrderDocumentRepository extends JpaRepository<ForceCancellOrderDocument, UUID>{
    public List<ForceCancellOrderDocument> findByEventStatus(String eventStatus);

    public List<ForceCancellOrderDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
