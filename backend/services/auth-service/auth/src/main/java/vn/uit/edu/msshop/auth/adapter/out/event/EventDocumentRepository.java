package vn.uit.edu.msshop.auth.adapter.out.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDocumentRepository extends MongoRepository<EventDocument, UUID> {
    public void deleteByReceiveAtBefore(Instant threshold);
}
