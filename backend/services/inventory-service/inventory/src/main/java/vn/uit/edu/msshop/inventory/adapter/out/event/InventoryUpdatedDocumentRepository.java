package vn.uit.edu.msshop.inventory.adapter.out.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryUpdatedDocumentRepository extends MongoRepository<InventoryUpdatedDocument, UUID> {
    public List<InventoryUpdatedDocument> findByEventStatus(String eventStatus);

    public List<InventoryUpdatedDocument> findTop50ByStatusOrderByCreatedAtAsc(String pending);

    public void deleteByStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
