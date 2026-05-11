package vn.uit.edu.msshop.cart.adapter.in.web;

import java.time.Instant;
import java.util.List;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.adapter.in.web.mapper.CartWebMapper;
import vn.uit.edu.msshop.cart.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.cart.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.cart.application.dto.command.DeleteCartItemCommand;
import vn.uit.edu.msshop.cart.application.port.in.DeleteCartItemUseCase;
import vn.uit.edu.msshop.cart.domain.event.OrderCreatedSuccess;

@Component
@KafkaListener(
        topics = "cart-topic",
        groupId = "order-group")
@RequiredArgsConstructor
public class CartEventListener {
    private final DeleteCartItemUseCase deleteItemUseCase;
    private final CartWebMapper mapper;
    private final EventDocumentRepository eventDocumentRepository;

    @KafkaHandler
    public void onOrderCreated(
            OrderCreatedSuccess orderCreatedSuccess) {
        if (eventDocumentRepository.existsById(orderCreatedSuccess.eventId()))
            return;
        List<DeleteCartItemCommand> commands = mapper.toCommand(orderCreatedSuccess);
        deleteItemUseCase.deleteManyItems(commands);
        eventDocumentRepository
                .save(EventDocument.builder().eventId(orderCreatedSuccess.eventId()).receiveAt(Instant.now()).build());
    }
}
