package vn.uit.edu.msshop.cart.adapter.in.web;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.adapter.in.web.mapper.CartWebMapper;
import vn.uit.edu.msshop.cart.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.cart.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.cart.application.dto.command.DeleteCartItemCommand;
import vn.uit.edu.msshop.cart.application.dto.command.UpdateCartInfoCommand;
import vn.uit.edu.msshop.cart.application.port.in.DeleteCartItemUseCase;
import vn.uit.edu.msshop.cart.application.port.in.UpdateCartInfoUseCase;
import vn.uit.edu.msshop.cart.application.port.out.VariantToUserPort;
import vn.uit.edu.msshop.cart.domain.event.ProductDeleted;
import vn.uit.edu.msshop.cart.domain.event.ProductUpdated;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;

@Component
@KafkaListener(topics="product-topic", groupId="product-group")
@RequiredArgsConstructor
public class CartProductEventListener {
    private final VariantToUserPort variantToUserPort;
    private final UpdateCartInfoUseCase updateCartInfoUseCase;
    private final CartWebMapper mapper;
    private final DeleteCartItemUseCase deleteItemUseCase;
    private final EventDocumentRepository eventDocumentRepo;

    @KafkaHandler
    public void updateCartItem(ProductUpdated event) {
        if(!eventDocumentRepo.existsById(event.getEventId())) {
            Set<String> userIds = variantToUserPort.getByVariantId(new VariantId(event.getVariantId()));
            List<UpdateCartInfoCommand> commands = userIds.stream().map(item->mapper.toCommand(event, item)).toList();
            updateCartInfoUseCase.updateInfo(commands);
            eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
        }
    }
    @KafkaHandler
    public void onVariantDelete(ProductDeleted event) {
        if(!eventDocumentRepo.existsById(event.getEventId())) {
            Set<String> userIds = variantToUserPort.getByVariantId(new VariantId(event.getVariantId()));
            List<DeleteCartItemCommand> commands = userIds.stream().map(item->new DeleteCartItemCommand(new UserId(UUID.fromString(item)), new VariantId(event.getVariantId()))).toList();
            deleteItemUseCase.deleteManyItems(commands);
            eventDocumentRepo.save(EventDocument.builder().eventId(event.getEventId()).receiveAt(Instant.now()).build());
        }

    }
}
