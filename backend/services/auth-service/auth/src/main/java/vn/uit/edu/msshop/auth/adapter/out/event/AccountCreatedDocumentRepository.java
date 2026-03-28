package vn.uit.edu.msshop.auth.adapter.out.event;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCreatedDocumentRepository extends MongoRepository<AccountCreatedDocument, UUID> {
    public List<AccountCreatedDocument> findByEventStatus(String eventStatus);
}
