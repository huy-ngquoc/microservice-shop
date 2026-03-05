package vn.uit.edu.msshop.image.application.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.application.dto.command.UploadImageCommand;
import vn.uit.edu.msshop.image.application.port.in.UploadImageUseCase;
import vn.uit.edu.msshop.image.application.port.out.PublishImageEventPort;
import vn.uit.edu.msshop.image.application.port.out.UploadImagePort;
import vn.uit.edu.msshop.image.domain.event.ImageUpdated;
@Component
@RequiredArgsConstructor
public class UploadImageService implements UploadImageUseCase {
    private final UploadImagePort uploadPort;
    private final PublishImageEventPort eventPort;

    @Override
    public void uploadImage(UploadImageCommand command) {
        uploadPort.upload(command.fileName(), command.objectId(), command.dataType(), command.bytes());
        eventPort.publish(new ImageUpdated(command.objectId(), command.dataType()));
    }

}
