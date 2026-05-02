package vn.uit.edu.msshop.recommendation.adapter.in.web;

import java.time.Instant;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.recommendation.adapter.in.web.mapper.VariantWebMapper;
import vn.uit.edu.msshop.recommendation.adapter.out.event.EventDocument;
import vn.uit.edu.msshop.recommendation.adapter.out.event.EventDocumentRepository;
import vn.uit.edu.msshop.recommendation.application.port.in.CreateVariantUseCase;
import vn.uit.edu.msshop.recommendation.application.port.in.DeleteVariantUseCase;
import vn.uit.edu.msshop.recommendation.application.port.in.UpdateVariantUseCase;
import vn.uit.edu.msshop.recommendation.domain.event.CreateVariantEvent;
import vn.uit.edu.msshop.recommendation.domain.event.DeleteVariantEvent;
import vn.uit.edu.msshop.recommendation.domain.event.UpdateVariantEvent;

@Component
@RequiredArgsConstructor
//@KafkaListener(topics="", groupId="")
public class VariantEventListener {
    private final CreateVariantUseCase createUseCase;
    private final UpdateVariantUseCase updateUseCase;
    private final DeleteVariantUseCase deleteUseCase;
    private final VariantWebMapper mapper;
    private final EventDocumentRepository eventDocumentRepo;

    //@KafkaHandler
    public void createVariantEventListener(CreateVariantEvent event) {
        if(!eventDocumentRepo.existsById(event.getEventId())) {
            createUseCase.create(mapper.toCommand(event));
            eventDocumentRepo.save(new EventDocument(event.getEventId(), Instant.now()));
        }
        
    }
    //@KafkaHandler
    public void updateVariantEventListener(UpdateVariantEvent event) {
        if(!eventDocumentRepo.existsById(event.getEventId())) {
            updateUseCase.update(mapper.toCommand(event));
            eventDocumentRepo.save(new EventDocument(event.getEventId(), Instant.now()));
        }
        
    }
    //@KafkaHandler
    public void deleteVariantEventListener(DeleteVariantEvent event) {
        if(!eventDocumentRepo.existsById(event.getEventId())) {
            deleteUseCase.delete(mapper.toCommand(event));
            eventDocumentRepo.save(new EventDocument(event.getEventId(), Instant.now()));
        }
        
    }
}
