package vn.uit.edu.payment.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.payment.application.port.out.PublishPaymentEventPort;
import vn.uit.edu.payment.domain.event.PaymentCreated;
import vn.uit.edu.payment.domain.event.PaymentUpdated;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher implements PublishPaymentEventPort {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(PaymentCreated event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publish(PaymentUpdated event) {
        publisher.publishEvent(event);
    }

}
