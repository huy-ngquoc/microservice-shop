package vn.uit.edu.msshop.rating.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.dto.command.UploadRatingImageCommand;
import vn.uit.edu.msshop.rating.application.dto.query.ImageView;
import vn.uit.edu.msshop.rating.application.port.in.UploadRatingImageUseCase;
import vn.uit.edu.msshop.rating.application.port.out.DeleteRatingImagePort;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingPort;
import vn.uit.edu.msshop.rating.application.port.out.SaveRatingPort;
import vn.uit.edu.msshop.rating.application.port.out.UploadRatingImagePort;
import vn.uit.edu.msshop.rating.domain.model.Rating;
import vn.uit.edu.msshop.rating.domain.model.valueobject.MediaPublicId;

@Service
@RequiredArgsConstructor
public class UploadImageService implements UploadRatingImageUseCase {
    private final LoadRatingPort loadPort;
    private final UploadRatingImagePort uploadImagePort;
    private final SaveRatingPort savePort;
    private final DeleteRatingImagePort deleteImagePort;

    @Override
    @Transactional
    public ImageView uploadRatingImage(
            UploadRatingImageCommand command) {
        final var rating = loadPort.loadById(command.ratingId());
        final var uploadedImage = uploadImagePort.upload(
                command.ratingId(),
                command.bytes(),
                command.originalFileName(),
                command.contentType());

        final var next = new Rating(
                rating.getId(),
                rating.getContent(),
                uploadedImage,
                rating.getProductId(),
                rating.getRatingPoint(),
                rating.getUserAvatar(),
                rating.getUserId(),
                rating.getUsername());

        if (rating.getMedia() != null) {
            deleteImagePort.deleteRatingImage(new MediaPublicId(rating.getMedia().publicId()));
        }

        savePort.save(next);
        return new ImageView(
                uploadedImage.url(),
                uploadedImage.publicId(),
                "IMAGE",
                uploadedImage.size());
    }
}
