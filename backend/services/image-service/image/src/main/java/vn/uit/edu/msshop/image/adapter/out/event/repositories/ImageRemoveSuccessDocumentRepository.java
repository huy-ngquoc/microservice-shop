package vn.uit.edu.msshop.image.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.image.adapter.out.event.documents.ImageRemoveSuccessDocument;

@Repository
public interface ImageRemoveSuccessDocumentRepository extends MongoRepository<ImageRemoveSuccessDocument, UUID> {
    public List<ImageRemoveSuccessDocument> findByEventStatus(String eventStatus);

    public List<ImageRemoveSuccessDocument> findTop50ByStatusOrderByCreatedAtAsc(String pending);

    public void deleteByStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
