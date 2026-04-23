package vn.uit.edu.msshop.inventory.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.inventory.adapter.out.event.documents.InventoryUpdatedDocument;

@Repository
public interface InventoryUpdatedDocumentRepository extends MongoRepository<InventoryUpdatedDocument, UUID> {
    public List<InventoryUpdatedDocument> findByEventStatus(String eventStatus);

    public List<InventoryUpdatedDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
    public List<InventoryUpdatedDocument> findByIsReadOrderByCreatedAtAsc(boolean isRead);
}
