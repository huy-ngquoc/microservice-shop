package vn.edu.uit.msshop.product.category.application.service;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryInfoCommand;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.UpdateCategoryInfoUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.SaveCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryUpdated;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;
import vn.edu.uit.msshop.product.shared.application.dto.Change;

@Service
@RequiredArgsConstructor
public class UpdateCategoryInfoService implements UpdateCategoryInfoUseCase {
    private final LoadCategoryPort loadPort;
    private final SaveCategoryPort savePort;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    public void updateInfo(
            final UpdateCategoryInfoCommand command) {
        final var nameSet = command.name().getSet();

        if (nameSet == null) {
            return;
        }

        final var category = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new CategoryNotFoundException(command.id()));

        final var next = this.applyChanges(category, nameSet);
        if (next == null) {
            return;
        }

        final var saved = this.savePort.save(next);
        this.eventPort.publish(new CategoryUpdated(saved.getId()));
    }

    private @Nullable Category applyChanges(
            final Category current,
            final Change.Set<CategoryName> nameSet) {
        if (nameSet.value().equals(current.getName())) {
            return null;
        }

        return new Category(
                current.getId(),
                nameSet.value(),
                current.getImageKey());
    }
}
