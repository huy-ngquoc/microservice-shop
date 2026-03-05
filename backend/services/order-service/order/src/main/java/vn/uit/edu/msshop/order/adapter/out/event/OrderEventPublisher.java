package vn.uit.edu.msshop.order.adapter.out.event;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.domain.event.OrderCreated;
import vn.uit.edu.msshop.order.domain.event.OrderUpdated;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher implements PublishOrderEventPort{
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(OrderCreated orderCreated) {
        eventPublisher.publishEvent(orderCreated);
    }

    @Override
    public void publish(OrderUpdated orderUpdated) {
        eventPublisher.publishEvent(orderUpdated);
    }

}
