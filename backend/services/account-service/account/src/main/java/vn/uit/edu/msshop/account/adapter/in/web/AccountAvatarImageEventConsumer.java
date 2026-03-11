package vn.uit.edu.msshop.account.adapter.in.web;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.application.port.in.UpdateAvatarUseCase;
import vn.uit.edu.msshop.account.domain.event.kafka.ImageRemoveSuccess;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics="image-topic",groupId="image-group")
public class AccountAvatarImageEventConsumer {
    private final UpdateAvatarUseCase updateAvatarUseCase;

    @KafkaHandler
    public void handleRemoveAvatarFolderSuccess(ImageRemoveSuccess event) {
        
    }

}
