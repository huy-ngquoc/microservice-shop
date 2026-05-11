package vn.uit.edu.msshop.order.adapter.in.web;

import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.adapter.out.persistence.OrderOutboxRepository;
import vn.uit.edu.msshop.order.adapter.out.persistence.OrderRepository;
import vn.uit.edu.msshop.order.domain.event.OrderCompositeEvent;
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDBEventListener {
    private final OrderRepository orderRepo;
    private final OrderOutboxRepository orderOutboxRepo;

    @RetryableTopic(
        attempts = "5", 
        backOff= @BackOff(delay = 5000, multiplier = 2.0)
    )
    @KafkaListener(topics = "order-save-fail-topic", groupId = "order-group")
    public void consumeOrderRetry(OrderCompositeEvent event) {
        event.getDocument().setStatus("PENDING");
        orderRepo.save(event.getDocument());
        orderOutboxRepo.save(event.getOutbox());
    }

    @DltHandler
    public void handleDlt(OrderCompositeEvent event) {
        log.error("Order failed, userId "+event.getDocument().getUserId().toString());
    }
}
