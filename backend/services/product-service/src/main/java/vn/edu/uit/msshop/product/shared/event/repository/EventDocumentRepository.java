package vn.edu.uit.msshop.product.shared.event.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vn.edu.uit.msshop.product.shared.event.document.EventDocument;

@Repository
public interface EventDocumentRepository extends MongoRepository<EventDocument, UUID> {

}
