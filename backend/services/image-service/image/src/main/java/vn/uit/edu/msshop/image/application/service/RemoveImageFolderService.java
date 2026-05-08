package vn.uit.edu.msshop.image.application.service;

import java.io.IOException;
import java.time.Instant;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.image.adapter.in.web.mapper.ImageWebMapper;
import vn.uit.edu.msshop.image.adapter.out.event.documents.ImageRemoveSuccessDocument;
import vn.uit.edu.msshop.image.adapter.out.event.repositories.ImageRemoveSuccessDocumentRepository;
import vn.uit.edu.msshop.image.application.dto.command.RemoveImageFolderCommand;
import vn.uit.edu.msshop.image.application.dto.command.RollbackImageFolderCommand;
import vn.uit.edu.msshop.image.application.port.in.RemoveImageFolderUseCase;
import vn.uit.edu.msshop.image.application.port.out.PublishImageEventPort;
import vn.uit.edu.msshop.image.application.port.out.RemoveImageFolderPort;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;
/*
private UUID eventId;
    private String url;
    private String publicId;
    private String fileName;
    private int width;
    private int height;
    private long size;
    private UUID objectId;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError; */
@Service
@RequiredArgsConstructor
@Slf4j
public class RemoveImageFolderService implements RemoveImageFolderUseCase {
    private final RemoveImageFolderPort removePort;
    private final PublishImageEventPort publishEventPort;
    //private final ImageEventMapper mapper;
    private final ImageWebMapper webMapper;
    private final ImageRemoveSuccessDocumentRepository imageRemoveSuccessDocumentRepo;

    @Override
    public void removeImageFolder(RemoveImageFolderCommand command) throws IOException {
        ImageInfo info = removePort.remove(command);
        //ImageRemoveSuccess event = mapper.toEvent(info,command.getObjectId().value());
        //publishEventPort.publishImageRemoveSuccess(event);
        imageRemoveSuccessDocumentRepo.save(
            ImageRemoveSuccessDocument.builder()
            .url(info.getUrl().value())
            .publicId(info.getPublicId().value())
            .fileName(info.getFileName().value())
            .width(info.getWidth().value())
            .height(info.getHeight().value())
            .size(info.getSize().value())
            .objectId(command.getObjectId().value())
            .eventStatus("PENDING")
            .createdAt(Instant.now())
            .retryCount(0)
            .updatedAt(null)
            .lastError(null).build()
        );

    }

    @Override
    public void rollbackImageFolder(RollbackImageFolderCommand command) throws IOException {
        RemoveImageFolderCommand c = webMapper.toCommand(command);
        removePort.remove(c);
    }
    
    

}
