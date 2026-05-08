package vn.uit.edu.msshop.image.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.application.dto.command.DeleteImageCommand;
import vn.uit.edu.msshop.image.application.port.in.DeleteImageUseCase;
import vn.uit.edu.msshop.image.application.port.out.DeleteImagePort;
@Service
@RequiredArgsConstructor
public class DeleteImageService implements DeleteImageUseCase {
    private final DeleteImagePort deletePort;
    
    @Override
    public void deleteImage(DeleteImageCommand command) {
        deletePort.deleteImage(command.publicId());
        
    }

}
