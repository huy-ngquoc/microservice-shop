package vn.uit.edu.msshop.order.adapter.in.web;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.port.in.UpdateOrderUseCase;
import vn.uit.edu.msshop.order.domain.event.inventory.ForceCancellOrder;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;

@Component
@RequiredArgsConstructor
@KafkaListener(topics="inventory-order",groupId="order-group")
public class OrderInventoryListener {
    private final UpdateOrderUseCase updateUseCase;
    @KafkaHandler
    public void onForceCancellOrder(ForceCancellOrder event) {
        updateUseCase.forceCancellOrder(new OrderId(event.orderId()));
    }
}
