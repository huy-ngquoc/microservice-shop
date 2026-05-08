package vn.uit.edu.msshop.account.adapter.out.event.repositories;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.uit.edu.msshop.account.adapter.out.event.documents.AccountCreatedDocument;

@Repository
public interface AccountCreatedDocumentRepository extends JpaRepository<AccountCreatedDocument, UUID> {
    public List<AccountCreatedDocument> findByEventStatus(String eventStatus);

    public List<AccountCreatedDocument> findTop50ByEventStatusOrderByCreatedAtAsc(String pending);

    public void deleteByEventStatusAndUpdatedAtBefore(String sent, Instant threshold);
}
