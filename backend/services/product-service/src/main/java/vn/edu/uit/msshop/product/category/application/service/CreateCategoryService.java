package vn.edu.uit.msshop.product.category.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.command.CreateCategoryCommand;
import vn.edu.uit.msshop.product.category.application.port.in.CreateCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.SaveCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryCreated;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;

@Service
@RequiredArgsConstructor
public class CreateCategoryService implements CreateCategoryUseCase {
    private final SaveCategoryPort savePort;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public void create(
            final CreateCategoryCommand command) {
        final var category = new Category(
                CategoryId.newId(),
                command.name(),
                null);

        final var saved = this.savePort.save(category);
        this.eventPort.publish(new CategoryCreated(saved.getId()));
    }
}
