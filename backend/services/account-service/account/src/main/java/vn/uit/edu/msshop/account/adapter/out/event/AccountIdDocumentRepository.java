package vn.uit.edu.msshop.account.adapter.out.event;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountIdDocumentRepository extends MongoRepository<AccountIdDocument, UUID> {
    public List<AccountIdDocument> findByEventStatus(String eventStatus);

    public List<AccountIdDocument> findTop50ByStatusOrderByCreatedAtAsc(String pending);

    public void deleteByStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
