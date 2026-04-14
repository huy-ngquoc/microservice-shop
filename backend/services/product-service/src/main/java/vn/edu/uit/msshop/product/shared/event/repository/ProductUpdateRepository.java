package vn.edu.uit.msshop.product.shared.event.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.edu.uit.msshop.product.shared.event.document.ProductUpdateDocument;

@Repository
public interface ProductUpdateRepository extends MongoRepository<ProductUpdateDocument, UUID> {
    public List<ProductUpdateDocument> findByEventStatus(String eventStatus);

    public List<ProductUpdateDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
