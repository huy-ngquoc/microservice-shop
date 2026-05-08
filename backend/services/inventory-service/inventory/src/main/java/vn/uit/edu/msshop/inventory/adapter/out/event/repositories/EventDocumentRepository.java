package vn.uit.edu.msshop.inventory.adapter.out.event.repositories;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.inventory.adapter.out.event.documents.EventDocument;

@Repository
public interface EventDocumentRepository extends JpaRepository<EventDocument, UUID> {

    public void deleteByReceiveAtBefore(Instant threshold);

}
