package vn.uit.edu.payment.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOnlinePaymentInfoJpaRepository extends JpaRepository<OnlinePaymentInfoJpaEntity, UUID> {
    public Optional<OnlinePaymentInfoJpaEntity> findByPaymentCode(
            Long paymentCode);

    public List<OnlinePaymentInfoJpaEntity> findByPayment_IdIn(
            List<UUID> paymentIds);
}
