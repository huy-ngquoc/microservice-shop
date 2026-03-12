package vn.uit.edu.payment.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPaymentJpaRepository extends JpaRepository<PaymentJpaEntity, UUID>{
    public Optional<PaymentJpaEntity> findFirstByOrderId(UUID orderId);

}
