package vn.uit.edu.payment.adapter.out.event.repositories;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.payment.adapter.out.event.documents.EventDocument;

@Repository
public interface EventDocumentRepository extends MongoRepository<EventDocument, UUID> {
    public void deleteByReceiveAtBefore(Instant threshold);
}
