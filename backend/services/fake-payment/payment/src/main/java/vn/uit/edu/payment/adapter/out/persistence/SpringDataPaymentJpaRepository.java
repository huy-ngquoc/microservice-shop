package vn.uit.edu.payment.adapter.out.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPaymentJpaRepository extends JpaRepository<PaymentJpaEntity, UUID> {
    public Optional<PaymentJpaEntity> findFirstByOrderId(
            UUID orderId);

    @Query("SELECT p FROM PaymentJpaEntity p WHERE p.paymentStatus = 'PENDING' AND p.paymentMethod = 'ONLINE' AND p.createAt < :timeout")
    List<PaymentJpaEntity> findExpiredPayments(
            @Param("timeout")
            Instant timeout);

    public long countByPaymentStatus(
            String status);

}
