package vn.uit.edu.msshop.inventory.adapter.in.web;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.inventory.adapter.in.web.mapper.InventoryWebMapper;
import vn.uit.edu.msshop.inventory.application.port.in.UpdateInventoryUseCase;
import vn.uit.edu.msshop.inventory.domain.event.OrderCancelled;
import vn.uit.edu.msshop.inventory.domain.event.OrderCreated;
import vn.uit.edu.msshop.inventory.domain.event.OrderShipped;

@Component
@RequiredArgsConstructor
@KafkaListener(topics="",groupId="")
public class InventoryOrderListener {
    private final InventoryWebMapper mapper;
    private final UpdateInventoryUseCase updateInventoryUseCase;

    @KafkaHandler
    public void onOrderCreated(OrderCreated event) {
        final var command = mapper.toCommand(event);
        updateInventoryUseCase.updateWhenOrderCreated(command);
    }
    @KafkaHandler
    public void onOrderCancelled(OrderCancelled event) {
        final var command = mapper.toCommand(event);
        updateInventoryUseCase.updateWhenOrderCancelled(command);
    }
    @KafkaHandler
    public void onOrderShipped(OrderShipped event) {
        final var command = mapper.toCommand(event);
        
    }
}
