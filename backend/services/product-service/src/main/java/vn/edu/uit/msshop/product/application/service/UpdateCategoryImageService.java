package vn.edu.uit.msshop.product.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.application.dto.command.UpdateCategoryImageCommand;
import vn.edu.uit.msshop.product.application.dto.query.CategoryImageView;
import vn.edu.uit.msshop.product.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.application.port.in.UpdateCategoryImageUseCase;
import vn.edu.uit.msshop.product.application.port.out.DeleteCategoryImagePort;
import vn.edu.uit.msshop.product.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.application.port.out.SaveCategoryPort;
import vn.edu.uit.msshop.product.application.port.out.UploadCategoryImagePort;
import vn.edu.uit.msshop.product.domain.event.category.CategoryUpdated;

@Service
@RequiredArgsConstructor
public class UpdateCategoryImageService implements UpdateCategoryImageUseCase {
    private final LoadCategoryPort loadPort;
    private final SaveCategoryPort savePort;
    private final UploadCategoryImagePort uploadImagePort;
    private final DeleteCategoryImagePort deleteImagePort;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public CategoryImageView updateImage(
            UpdateCategoryImageCommand command) {
        final var oldCategory = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new CategoryNotFoundException(command.id()));

        final var uploadedImage = this.uploadImagePort.upload(
                command.id(),
                command.bytes(),
                command.originalFilename(),
                command.contentType());

        final var newCategory = oldCategory.withImage(uploadedImage);

        final var imageView = new CategoryImageView(
                uploadedImage.url().value(),
                uploadedImage.size().width(),
                uploadedImage.size().height());

        if (newCategory == oldCategory) {
            return imageView;
        }

        final var oldImage = oldCategory.getImage();
        if ((oldImage != null) && (!oldImage.key().equals(uploadedImage.key()))) {
            this.deleteImagePort.deleteByKey(oldImage.key());
        }

        final var saved = this.savePort.save(newCategory);
        this.eventPort.publish(new CategoryUpdated(saved.getId()));

        return imageView;
    }
}
