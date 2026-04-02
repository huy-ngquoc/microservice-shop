package vn.uit.edu.msshop.image.adapter.in.web;

import java.io.IOException;
import java.time.Instant;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.image.adapter.in.web.mapper.ImageWebMapper;
import vn.uit.edu.msshop.image.adapter.out.event.documents.EventDocument;
import vn.uit.edu.msshop.image.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.image.application.port.in.DeleteImageUseCase;
import vn.uit.edu.msshop.image.application.port.in.RemoveImageFolderUseCase;
import vn.uit.edu.msshop.image.domain.event.DeleteOldImageEvent;
import vn.uit.edu.msshop.image.domain.event.RollbackImageEvent;

@Component
@KafkaListener(topics="image-account-topic")
@RequiredArgsConstructor
@Slf4j
public class ImageEventListener {
    private final DeleteImageUseCase deleteUseCase;
    private final RemoveImageFolderUseCase removeUseCase;
    private final ImageWebMapper mapper;
    private final EventDocumentRepository eventDocumentRepo;
    
    @KafkaHandler
    @Transactional
    public void handleDeleteOldImage(DeleteOldImageEvent event) {
        if(!eventDocumentRepo.existsById(event.eventId())) {
            final var command = mapper.toCommand(event.oldImagePublicId());
            deleteUseCase.deleteImage(command);
            eventDocumentRepo.save(EventDocument.builder().eventId(event.eventId()).receiveAt(Instant.now()).build());
        }
        
    }

    @KafkaHandler
    @Transactional
    public void handleRollback(RollbackImageEvent event) throws IOException {
        if(!eventDocumentRepo.existsById(event.eventId())) {
            final var command = mapper.toCommand(event);
            removeUseCase.rollbackImageFolder(command);
            eventDocumentRepo.save(EventDocument.builder().eventId(event.eventId()).receiveAt(Instant.now()).build());
        }
        
    }



}
