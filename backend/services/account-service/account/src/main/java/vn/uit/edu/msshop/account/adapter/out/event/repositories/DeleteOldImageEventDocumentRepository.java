package vn.uit.edu.msshop.account.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.account.adapter.out.event.documents.DeleteOldImageEventDocument;

@Repository
public interface DeleteOldImageEventDocumentRepository extends JpaRepository<DeleteOldImageEventDocument, UUID> {
    public List<DeleteOldImageEventDocument> findByEventStatus(String eventStatus);

    public List<DeleteOldImageEventDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
