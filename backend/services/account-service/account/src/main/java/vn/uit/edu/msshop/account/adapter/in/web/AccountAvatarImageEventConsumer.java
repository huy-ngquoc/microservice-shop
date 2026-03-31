package vn.uit.edu.msshop.account.adapter.in.web;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.adapter.in.web.mapper.AccountWebMapper;
import vn.uit.edu.msshop.account.adapter.out.event.EventDocument;
import vn.uit.edu.msshop.account.adapter.out.event.EventDocumentRepository;
import vn.uit.edu.msshop.account.application.port.in.UpdateAvatarUseCase;
import vn.uit.edu.msshop.account.domain.event.kafka.ImageRemoveSuccess;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics="image-topic",groupId="image-group")
public class AccountAvatarImageEventConsumer {
    private final UpdateAvatarUseCase updateAvatarUseCase;
    private final AccountWebMapper mapper;
    private final EventDocumentRepository eventDocumentRepo;

    @KafkaHandler
    public void handleRemoveAvatarFolderSuccess(ImageRemoveSuccess event) {
        if(!eventDocumentRepo.existsById(event.getEventId())) {
            final var command = mapper.toCommand(event);
            updateAvatarUseCase.updateAvatar(command);
            eventDocumentRepo.save(new EventDocument(event.getEventId(), Instant.now()));
        }
    }

}
