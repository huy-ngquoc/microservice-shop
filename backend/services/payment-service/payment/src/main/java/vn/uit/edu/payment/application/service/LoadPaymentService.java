package vn.uit.edu.payment.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.application.dto.query.PaymentView;
import vn.uit.edu.payment.application.exception.PaymentNotFoundException;
import vn.uit.edu.payment.application.mapper.PaymentViewMapper;
import vn.uit.edu.payment.application.port.in.LoadPaymentUseCase;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentId;

@Service
@RequiredArgsConstructor
public class LoadPaymentService implements LoadPaymentUseCase {
    private final LoadPaymentPort loadPort;
    private final PaymentViewMapper mapper;

    @Override
    public PaymentView findById(PaymentId paymentId) {
        return mapper.toView(loadPort.loadPaymentById(paymentId).orElseThrow(()->new PaymentNotFoundException(paymentId)));
    }

    @Override
    public PaymentView loadByOrderId(OrderId orderId) {
        final var result = loadPort.loadPaymentByOrderId(orderId);
        if(result==null) return null;
       return mapper.toView(result);
    }

    @Override
    public List<Payment> loadExpiredPayment(Instant timeout) {
        return loadPort.loadExpiredPayment(timeout);
    }

    
}
