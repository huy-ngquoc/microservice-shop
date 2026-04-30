package vn.uit.edu.payment.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaybackPaymentRepository extends JpaRepository<PaybackPayments, UUID> {

}
