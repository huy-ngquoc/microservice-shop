package vn.uit.edu.msshop.account.adapter.out.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RollbackImageEventDocumentRepository extends MongoRepository<RollbackImageEventDocument, UUID> {
    public List<RollbackImageEventDocument> findByEventStatus(String eventStatus);

    public List<RollbackImageEventDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
