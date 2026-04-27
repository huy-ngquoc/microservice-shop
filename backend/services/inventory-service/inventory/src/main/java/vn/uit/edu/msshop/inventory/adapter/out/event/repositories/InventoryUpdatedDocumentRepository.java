package vn.uit.edu.msshop.inventory.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.inventory.adapter.out.event.documents.InventoryUpdatedDocument;

@Repository
public interface InventoryUpdatedDocumentRepository extends JpaRepository<InventoryUpdatedDocument, UUID> {
    public List<InventoryUpdatedDocument> findByEventStatus(String eventStatus);

    public List<InventoryUpdatedDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
    public Page<InventoryUpdatedDocument> findByIsReadOrderByCreatedAtAsc(boolean isRead,Pageable pageable );
}
