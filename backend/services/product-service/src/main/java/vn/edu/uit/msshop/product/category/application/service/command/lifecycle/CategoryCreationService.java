package vn.edu.uit.msshop.product.category.application.service.command.lifecycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategoryCreationCommand;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle.CategoryCreationUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.command.CategoryCreationPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryCreatedEvent;
import vn.edu.uit.msshop.product.category.domain.model.creation.NewCategory;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryName;

@Service
@RequiredArgsConstructor
public class CategoryCreationService
        implements CategoryCreationUseCase {

    private final CategoryCreationPort creationPort;

    private final CategoryViewMapper mapper;
    private final CategoryEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.CATEGORY_LIST,
            allEntries = true)
    public CategoryView create(
            final CategoryCreationCommand cmd) {
        final var categoryName = new CategoryName(cmd.categoryName());

        final var newCategory = new NewCategory(
                CategoryId.newId(),
                categoryName);
        final var saved = this.creationPort.create(newCategory);

        final var event = new CategoryCreatedEvent(saved.getId());
        this.eventPublicationPort.publishEvent(event);

        return this.mapper.toView(saved);
    }
}
