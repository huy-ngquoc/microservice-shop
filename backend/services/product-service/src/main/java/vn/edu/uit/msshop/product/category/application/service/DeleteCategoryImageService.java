package vn.edu.uit.msshop.product.category.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.DeleteCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.DeleteCategoryImagePort;
import vn.edu.uit.msshop.product.category.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.SaveCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryUpdated;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;

@Service
@RequiredArgsConstructor
public class DeleteCategoryImageService implements DeleteCategoryImageUseCase {
    private final LoadCategoryPort loadPort;
    private final SaveCategoryPort savePort;
    private final DeleteCategoryImagePort deleteImagePort;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public void deleteById(
            final CategoryId id) {
        final var oldCategory = this.loadPort.loadById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        final var oldImage = oldCategory.getImage();
        if (oldImage == null) {
            return;
        }

        this.deleteImagePort.deleteByKey(oldImage.key());
        final var newCategory = oldCategory.withoutImage();
        final var saved = this.savePort.save(newCategory);
        this.eventPort.publish(new CategoryUpdated(saved.getId()));
    }
}
