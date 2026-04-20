package vn.uit.edu.msshop.notification.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<EmailDocument, UUID> {
    public List<EmailDocument> findByEmailStatus(String status);

}
