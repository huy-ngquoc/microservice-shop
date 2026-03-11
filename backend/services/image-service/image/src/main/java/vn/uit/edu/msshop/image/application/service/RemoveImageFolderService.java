package vn.uit.edu.msshop.image.application.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.image.adapter.in.web.mapper.ImageWebMapper;
import vn.uit.edu.msshop.image.adapter.out.event.ImageEventMapper;
import vn.uit.edu.msshop.image.application.dto.command.RemoveImageFolderCommand;
import vn.uit.edu.msshop.image.application.dto.command.RollbackImageFolderCommand;
import vn.uit.edu.msshop.image.application.port.in.RemoveImageFolderUseCase;
import vn.uit.edu.msshop.image.application.port.out.PublishImageEventPort;
import vn.uit.edu.msshop.image.application.port.out.RemoveImageFolderPort;
import vn.uit.edu.msshop.image.domain.event.ImageRemoveSuccess;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;
@Service
@RequiredArgsConstructor
@Slf4j
public class RemoveImageFolderService implements RemoveImageFolderUseCase {
    private final RemoveImageFolderPort removePort;
    private final PublishImageEventPort publishEventPort;
    private final ImageEventMapper mapper;
    private final ImageWebMapper webMapper;

    @Override
    public void removeImageFolder(RemoveImageFolderCommand command) throws IOException {
        ImageInfo info = removePort.remove(command);
        ImageRemoveSuccess event = mapper.toEvent(info,command.getObjectId().value());
        publishEventPort.publishImageRemoveSuccess(event);

    }

    @Override
    public void rollbackImageFolder(RollbackImageFolderCommand command) throws IOException {
        RemoveImageFolderCommand c = webMapper.toCommand(command);
        removePort.remove(c);
    }
    
    

}
