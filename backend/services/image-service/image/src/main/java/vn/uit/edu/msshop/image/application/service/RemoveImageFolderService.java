package vn.uit.edu.msshop.image.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.image.adapter.out.event.ImageEventMapper;
import vn.uit.edu.msshop.image.adapter.out.event.ImageEventPublisher;
import vn.uit.edu.msshop.image.application.dto.command.RemoveAvatarImageCommand;
import vn.uit.edu.msshop.image.application.dto.command.RemoveProductImageCommand;
import vn.uit.edu.msshop.image.application.dto.command.RemoveVariantImageCommand;
import vn.uit.edu.msshop.image.application.port.in.RemoveImageFolderUseCase;
import vn.uit.edu.msshop.image.application.port.out.RemoveImageFolderPort;
import vn.uit.edu.msshop.image.domain.event.ImageRemoveSuccess;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;
@Service
@RequiredArgsConstructor
@Slf4j
public class RemoveImageFolderService implements RemoveImageFolderUseCase {
    private final RemoveImageFolderPort removePort;
    private final ImageEventMapper mapper;
    private final ImageEventPublisher publisher;
    @Override
    public void removeProduct(RemoveProductImageCommand command) {
        ImageInfo info = removePort.remove(command);
        ImageRemoveSuccess event = mapper.toEvent(info);
        publisher.publishRemoveImageEvent(event);
    }

    @Override
    public void removeAvatar(RemoveAvatarImageCommand command) {
        ImageInfo info = removePort.remove(command);
        ImageRemoveSuccess event = mapper.toEvent(info);
        publisher.publishRemoveImageEvent(event);
    }

    @Override
    public void removeVariant(RemoveVariantImageCommand command) {
        ImageInfo info = removePort.remove(command);
        ImageRemoveSuccess event = mapper.toEvent(info);
        publisher.publishRemoveImageEvent(event);
    }

}
