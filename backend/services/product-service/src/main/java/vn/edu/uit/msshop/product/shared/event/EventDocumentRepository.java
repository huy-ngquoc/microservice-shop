package vn.edu.uit.msshop.product.shared.event;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDocumentRepository extends MongoRepository<EventDocument, UUID> {
    
}
