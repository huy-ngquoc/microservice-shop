package vn.uit.edu.payment.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.adapter.out.persistence.mapper.PaymentEntityMapper;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;

@Component
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements LoadPaymentPort, SavePaymentPort {
    private final PaymentEntityMapper mapper;
    private final SpringDataPaymentJpaRepository repository;
    @Override
    public Optional<Payment> loadPaymentById(PaymentId paymentId) {
        Optional<PaymentJpaEntity> paymentJpaEntity = repository.findById(paymentId.value());
        return paymentJpaEntity.map(mapper::toDomain);
    }

    @Override
    public Payment loadPaymentByOrderId(OrderId orderId) {
        PaymentJpaEntity paymentJpaEntity = repository.findByOrderId(orderId.value());
        return mapper.toDomain(paymentJpaEntity);
    }

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity paymentJpaEntity = mapper.toEntity(payment);
        PaymentJpaEntity result = repository.save(paymentJpaEntity);
        return mapper.toDomain(result);
    }

}
