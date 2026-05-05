package vn.edu.uit.msshop.product.shared.event.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.edu.uit.msshop.product.shared.event.document.VariantDeletedDocument;

@Repository
public interface VariantDeletedRepository extends MongoRepository<VariantDeletedDocument, UUID> {
  public List<VariantDeletedDocument> findByEventStatus(String eventStatus);

  public List<VariantDeletedDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

  public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
