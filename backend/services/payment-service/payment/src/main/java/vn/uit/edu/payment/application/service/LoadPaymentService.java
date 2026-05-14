package vn.uit.edu.payment.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.application.dto.query.PaymentView;
import vn.uit.edu.payment.application.exception.PaymentNotFoundException;
import vn.uit.edu.payment.application.mapper.PaymentViewMapper;
import vn.uit.edu.payment.application.port.in.LoadPaymentUseCase;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.bootstrap.config.cache.CacheNames;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;

@Service
@RequiredArgsConstructor
public class LoadPaymentService implements LoadPaymentUseCase {
    private final LoadPaymentPort loadPort;
    private final PaymentViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.PAYMENT_BY_ID,
            key = "#paymentId.value()")
    public PaymentView findById(
            final PaymentId paymentId) {
        final var result = this.loadPort.loadPaymentById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        return mapper.toView(result);
    }

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.PAYMENT_BY_ORDER_ID,
            key = "#orderId.value()")
    public PaymentView loadByOrderId(
            final OrderId orderId) {
        final var result = loadPort.loadPaymentByOrderId(orderId);
        if (result == null) {
            return null;
        }
        return mapper.toView(result);
    }

    @Override
    @Transactional(
            readOnly = true)
    public List<Payment> loadExpiredPayment(
            final Instant timeout) {
        return loadPort.loadExpiredPayment(timeout);
    }

}
