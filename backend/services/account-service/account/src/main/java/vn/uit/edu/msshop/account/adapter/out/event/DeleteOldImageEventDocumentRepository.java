package vn.uit.edu.msshop.account.adapter.out.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeleteOldImageEventDocumentRepository extends MongoRepository<DeleteOldImageEventDocument, UUID> {
    public List<DeleteOldImageEventDocument> findByEventStatus(String eventStatus);

    public List<DeleteOldImageEventDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
