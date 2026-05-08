package vn.uit.edu.msshop.account.adapter.out.persistence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountOutboxEntityRepository extends JpaRepository<AccountOutboxEntity,UUID> {
    public List<AccountOutboxEntity> findByIsCheck(boolean isCheck);

    public List<AccountOutboxEntity> findTop50ByIsCheckOrderByCreatedAtAsc(boolean isCheck);

    public void deleteByIsCheckAndUpdatedAtBefore(boolean isCheck, Instant threshold);
}
