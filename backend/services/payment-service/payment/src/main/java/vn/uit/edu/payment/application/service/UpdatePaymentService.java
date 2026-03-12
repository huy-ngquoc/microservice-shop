package vn.uit.edu.payment.application.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.application.dto.command.UpdatePaymentCommand;
import vn.uit.edu.payment.application.dto.query.PaymentView;
import vn.uit.edu.payment.application.exception.PaymentNotFoundException;
import vn.uit.edu.payment.application.mapper.PaymentViewMapper;
import vn.uit.edu.payment.application.port.in.UpdatePaymentUseCase;
import vn.uit.edu.payment.application.port.out.LoadPaymentPort;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.application.port.out.SavePaymentPort;
import vn.uit.edu.payment.domain.event.OnlinePaymentCancelled;
import vn.uit.edu.payment.domain.event.OnlinePaymentExpired;
import vn.uit.edu.payment.domain.event.PaymentUpdated;
import vn.uit.edu.payment.domain.model.Payment;
import vn.uit.edu.payment.domain.model.valueobject.OrderId;
import vn.uit.edu.payment.domain.model.valueobject.PaymentStatus;

@Service
@RequiredArgsConstructor
public class UpdatePaymentService implements UpdatePaymentUseCase {
    private final LoadPaymentPort loadPort;
    private final SavePaymentPort savePort;
    private final PublishPaymentEventPort eventPort;
    private final PaymentViewMapper mapper;

    @Override
    @Transactional
    public PaymentView update(UpdatePaymentCommand command) {
        final var payment = loadPort.loadPaymentById(command.paymentId()).orElseThrow(()->new PaymentNotFoundException(command.paymentId()));
        final var update = Payment.UpdateInfo.builder().paymentId(command.paymentId()).currency(command.currency().apply(payment.getCurrency()))
        .paymentMethod(command.paymentMethod().apply(payment.getPaymentMethod())).paymentStatus(command.paymentStatus().apply(payment.getPaymentStatus()))
        .build();
        final var next = payment.applyUpdateInfo(update);
        final Payment saved = savePort.save(next);
        eventPort.publish(new PaymentUpdated(saved.getPaymentId()));
        return mapper.toView(saved);
    }

    @Override
    public void onlinePaymentExpire(OrderId orderId) {
        final var payment = loadPort.loadPaymentByOrderId(orderId);
        if(payment==null) return;
        final var update = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId()).currency(payment.getCurrency())
        .paymentMethod(payment.getPaymentMethod()).paymentStatus(new PaymentStatus("EXPIRED"))
        .build();
        final var next = payment.applyUpdateInfo(update);
        final Payment saved = savePort.save(next);
        eventPort.publishPaymentExpired(new OnlinePaymentExpired(orderId.value()));
    }

    @Override
    public void onlinePaymentCancelled(OrderId orderId) {
        final var payment = loadPort.loadPaymentByOrderId(orderId);
        if(payment==null) return;
        final var update = Payment.UpdateInfo.builder().paymentId(payment.getPaymentId()).currency(payment.getCurrency())
        .paymentMethod(payment.getPaymentMethod()).paymentStatus(new PaymentStatus("CANCELLED"))
        .build();
        final var next = payment.applyUpdateInfo(update);
        final Payment saved = savePort.save(next);
        eventPort.publishPaymentCancelled(new OnlinePaymentCancelled(orderId.value()));
    }

}
