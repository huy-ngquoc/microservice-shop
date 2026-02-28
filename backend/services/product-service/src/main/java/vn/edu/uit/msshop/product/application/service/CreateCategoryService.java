package vn.edu.uit.msshop.product.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.application.dto.command.CreateCategoryCommand;
import vn.edu.uit.msshop.product.application.port.in.CreateCategoryUseCase;
import vn.edu.uit.msshop.product.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.application.port.out.SaveCategoryPort;
import vn.edu.uit.msshop.product.domain.event.category.CategoryCreated;
import vn.edu.uit.msshop.product.domain.model.category.Category;
import vn.edu.uit.msshop.product.domain.model.category.command.CategoryDraft;

@Service
@RequiredArgsConstructor
public class CreateCategoryService implements CreateCategoryUseCase {
    private final SaveCategoryPort savePort;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public void create(
            final CreateCategoryCommand command) {
        final var draft = CategoryDraft.builder()
                .name(command.name())
                .build();
        final var category = Category.create(draft);
        final var saved = this.savePort.save(category);

        this.eventPort.publish(new CategoryCreated(saved.getId()));
    }
}
