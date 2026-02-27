package vn.uit.edu.msshop.account.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SpringDataAccountJpaRepository extends JpaRepository<AccountJpaEntity, UUID>{

}
