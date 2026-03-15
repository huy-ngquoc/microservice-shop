package vn.edu.uit.msshop.product.category.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.dto.command.CreateCategoryCommand;
import vn.edu.uit.msshop.product.category.application.exception.CategoryImageKeyNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.CreateCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.CategoryImageStoragePort;
import vn.edu.uit.msshop.product.category.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.SaveCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryCreated;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCategoryService implements CreateCategoryUseCase {
    private final SaveCategoryPort savePort;
    private final CategoryImageStoragePort imageStoragePort;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public void create(
            final CreateCategoryCommand command) {
        this.ensureImageKeyExistsInTemp(command.imageKey());

        final var category = new Category(
                CategoryId.newId(),
                command.name(),
                command.imageKey());

        final var saved = this.publishImageAndSave(category);

        this.eventPort.publish(new CategoryCreated(saved.getId()));
    }

    private void ensureImageKeyExistsInTemp(
            final CategoryImageKey imageKey) {
        if (!this.imageStoragePort.existsAsTemp(imageKey)) {
            throw new CategoryImageKeyNotFoundException(imageKey);
        }
    }

    private Category publishImageAndSave(
            final Category category) {
        this.imageStoragePort.publishImage(category.getImageKey());

        try {
            return this.savePort.save(category);
        } catch (final RuntimeException e) {
            try {
                this.imageStoragePort.unpublishImage(category.getImageKey());
            } catch (final RuntimeException compensateEx) {
                e.addSuppressed(compensateEx);
                log.error("Compensation failed for key '{}'", category.getImageKey().value(), compensateEx);
            }
            throw e;
        }
    }
}
