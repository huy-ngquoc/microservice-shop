package vn.edu.uit.msshop.product.category.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.dto.command.CreateCategoryCommand;
import vn.edu.uit.msshop.product.category.application.exception.CategoryImageKeyNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.CreateCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.MoveCategoryImagePort;
import vn.edu.uit.msshop.product.category.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.SaveCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.VerifyCategoryImageKeyPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryCreated;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCategoryService implements CreateCategoryUseCase {
    private final SaveCategoryPort savePort;
    private final VerifyCategoryImageKeyPort verifyImageKeyPort;
    private final MoveCategoryImagePort moveImagePort;
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

        this.moveImagePort.moveToCategory(command.imageKey());
        final var saved = this.saveWithCompensation(category, command.imageKey());

        this.eventPort.publish(new CategoryCreated(saved.getId()));
    }

    private void ensureImageKeyExistsInTemp(
            final CategoryImageKey imageKey) {
        if (!this.verifyImageKeyPort.existsInTemp(imageKey)) {
            throw new CategoryImageKeyNotFoundException(imageKey);
        }
    }

    private Category saveWithCompensation(
            final Category category,
            final CategoryImageKey imageKey) {
        try {
            return this.savePort.save(category);
        } catch (final RuntimeException e) {
            try {
                this.moveImagePort.moveBackToTemp(imageKey);
            } catch (final RuntimeException compensateEx) {
                e.addSuppressed(compensateEx);
                log.error("Compensation failed for key '{}'", imageKey.value(), compensateEx);
            }
            throw e;
        }
    }
}
