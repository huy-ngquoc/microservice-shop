package vn.uit.edu.msshop.rating.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.port.in.DeleteRatingImageUseCase;
import vn.uit.edu.msshop.rating.application.port.out.DeleteRatingImagePort;
import vn.uit.edu.msshop.rating.domain.model.valueobject.MediaPublicId;

@Service
@RequiredArgsConstructor
public class DeleteImageService implements DeleteRatingImageUseCase{
    private final DeleteRatingImagePort deletePort;

    @Override
    public void delete(MediaPublicId id) {
        deletePort.deleteRatingImage(id);
    }

    
}
