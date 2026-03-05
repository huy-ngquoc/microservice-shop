package vn.uit.edu.msshop.image.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.application.dto.command.DeleteImageCommand;
import vn.uit.edu.msshop.image.application.port.in.DeleteImageUseCase;
import vn.uit.edu.msshop.image.application.port.out.DeleteImagePort;
import vn.uit.edu.msshop.image.application.port.out.PublishImageEventPort;
import vn.uit.edu.msshop.image.domain.event.ImageDeleted;
@Service
@RequiredArgsConstructor
public class DeleteImageService implements DeleteImageUseCase {
    private final DeleteImagePort deletePort;
    private final PublishImageEventPort eventPort;
    @Override
    public void deleteImage(DeleteImageCommand command) {
        deletePort.deleteImage(command.publicId(), command.objectId(), command.dataType());
        eventPort.publish(new ImageDeleted(command.objectId(), command.dataType()));
    }

}
