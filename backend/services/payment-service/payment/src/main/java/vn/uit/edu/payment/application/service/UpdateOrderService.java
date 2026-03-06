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
import vn.uit.edu.payment.domain.event.PaymentUpdated;
import vn.uit.edu.payment.domain.model.Payment;

@Service
@RequiredArgsConstructor
public class UpdateOrderService implements UpdatePaymentUseCase {
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

}
