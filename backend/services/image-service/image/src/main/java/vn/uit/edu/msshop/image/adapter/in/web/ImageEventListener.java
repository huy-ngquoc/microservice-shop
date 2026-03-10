package vn.uit.edu.msshop.image.adapter.in.web;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.adapter.in.web.mapper.ImageWebMapper;
import vn.uit.edu.msshop.image.application.dto.command.RemoveAvatarImageCommand;
import vn.uit.edu.msshop.image.application.dto.command.RemoveProductImageCommand;
import vn.uit.edu.msshop.image.application.dto.command.RemoveVariantImageCommand;
import vn.uit.edu.msshop.image.application.port.in.RemoveImageFolderUseCase;
import vn.uit.edu.msshop.image.domain.event.AvatarImageEvent;
import vn.uit.edu.msshop.image.domain.event.ProductImageEvent;
import vn.uit.edu.msshop.image.domain.event.VariantImageEvent;

@Component
@KafkaListener(topics="image-remove-topic",groupId="image-group")
@RequiredArgsConstructor
public class ImageEventListener {
    private final RemoveImageFolderUseCase removeUseCase;
    private final ImageWebMapper mapper;
    @KafkaHandler
    public void handleRemoveProductImage(ProductImageEvent event) {
        RemoveProductImageCommand command = mapper.toCommand(event);
        removeUseCase.removeProduct(command);
    } 

    @KafkaHandler
    public void handleRemoveVariantImage(VariantImageEvent event) {
        RemoveVariantImageCommand command = mapper.toCommand(event);
        removeUseCase.removeVariant(command);
    } 
    @KafkaHandler
    public void handleRemoveAvatarImage(AvatarImageEvent event) {
        RemoveAvatarImageCommand command = mapper.toCommand(event);
        removeUseCase.removeAvatar(command);
    }
}
