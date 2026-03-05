package vn.uit.edu.msshop.image.application.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.application.dto.command.UploadImageCommand;
import vn.uit.edu.msshop.image.application.port.in.UploadImageUseCase;
import vn.uit.edu.msshop.image.application.port.out.PublishImageEventPort;
import vn.uit.edu.msshop.image.application.port.out.UploadImagePort;
import vn.uit.edu.msshop.image.domain.event.ImageUpdated;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;
@Component
@RequiredArgsConstructor
public class UploadImageService implements UploadImageUseCase {
    private final UploadImagePort uploadPort;
    private final PublishImageEventPort eventPort;

    @Override
    public ImageInfo uploadImage(UploadImageCommand command) {
        ImageInfo info = uploadPort.upload(command.fileName(), command.objectId(), command.dataType(), command.bytes());
        eventPort.publish(new ImageUpdated(command.objectId(), command.dataType()));
        return info;
    }

}
