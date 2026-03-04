package vn.edu.uit.msshop.product.category.application.service;

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
import vn.edu.uit.msshop.product.category.domain.model.mutation.CategoryUpdateInfo;

@Service
@RequiredArgsConstructor
public class UpdateCategoryInfoService implements UpdateCategoryInfoUseCase {
    private final LoadCategoryPort loadPort;
    private final SaveCategoryPort savePort;
    private final PublishCategoryEventPort eventPort;

    @Override
    @Transactional
    @SuppressWarnings("ReferenceEquality")
    public void updateInfo(
            final UpdateCategoryInfoCommand command) {
        final var category = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new CategoryNotFoundException(command.id()));

        final var update = CategoryUpdateInfo.builder()
                .name(command.name().apply(category.getName()))
                .build();
        final var next = category.applyUpdateInfo(update);

        if (next == category) {
            return;
        }

        final var saved = this.savePort.save(next);
        this.eventPort.publish(new CategoryUpdated(saved.getId()));
    }
}
