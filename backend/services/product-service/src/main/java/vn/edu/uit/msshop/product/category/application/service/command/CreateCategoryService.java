package vn.edu.uit.msshop.product.category.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.dto.command.CreateCategoryCommand;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.command.CreateCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.CreateCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryCreated;
import vn.edu.uit.msshop.product.category.domain.model.creation.NewCategory;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCategoryService implements CreateCategoryUseCase {
    private final CreateCategoryPort createPort;
    private final CategoryViewMapper mapper;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public CategoryView create(
            final CreateCategoryCommand command) {
        final var newCategory = new NewCategory(
                CategoryId.newId(),
                command.name());

        final var saved = this.createPort.create(newCategory);

        this.eventPort.publish(new CategoryCreated(saved.getId()));

        return this.mapper.toView(saved);
    }
}
