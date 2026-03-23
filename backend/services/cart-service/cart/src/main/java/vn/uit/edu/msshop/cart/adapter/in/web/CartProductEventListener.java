package vn.uit.edu.msshop.cart.adapter.in.web;

import java.util.List;
import java.util.Set;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.adapter.in.web.mapper.CartWebMapper;
import vn.uit.edu.msshop.cart.application.dto.command.UpdateCartInfoCommand;
import vn.uit.edu.msshop.cart.application.port.in.UpdateCartInfoUseCase;
import vn.uit.edu.msshop.cart.application.port.out.VariantToUserPort;
import vn.uit.edu.msshop.cart.domain.event.ProductUpdated;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;

@Component
@KafkaListener(topics="product-topic", groupId="product-group")
@RequiredArgsConstructor
public class CartProductEventListener {
    private final VariantToUserPort variantToUserPort;
    private final UpdateCartInfoUseCase updateCartInfoUseCase;
    private final CartWebMapper mapper;
    @KafkaHandler
    public void updateCartItem(ProductUpdated event) {
        Set<String> userIds = variantToUserPort.getByVariantId(new VariantId(event.getVariantId()));
        List<UpdateCartInfoCommand> commands = userIds.stream().map(item->mapper.toCommand(event, item)).toList();
        updateCartInfoUseCase.updateInfo(commands);
    }
}
