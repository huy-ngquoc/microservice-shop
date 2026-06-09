package vn.edu.uit.msshop.product.category.application.service.command.lifecycle;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.dto.command.CategoryLifecycleCommands;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle.CategoryHardDeletionByIdUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.DeleteCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadSoftDeletedCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.validation.CheckCategoryHasSoftDeletedProductsPort;
import vn.edu.uit.msshop.product.category.application.service.command.image.CategoryImageDeleter;
import vn.edu.uit.msshop.product.category.application.service.command.support.CategoryVersionGuard;
import vn.edu.uit.msshop.product.category.domain.event.CategoryHardDeletedEvent;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryHardDeletionByIdService
        implements CategoryHardDeletionByIdUseCase {

    private final LoadSoftDeletedCategoryPort loadSoftDeletedPort;
    private final DeleteCategoryPort deletePort;

    private final CheckCategoryHasSoftDeletedProductsPort checkHasSoftDeletedProductsPort;

    private final CategoryImageDeleter imageDeleter;

    private final CategoryEventPublicationPort eventPort;

    @Override
    @Transactional
    public void hardDelete(
            final CategoryLifecycleCommands.HardDelete cmd) {
        final var categoryId = cmd.id();
        final var category = this.loadSoftDeletedPort.loadSoftDeletedById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        CategoryVersionGuard.ensureMatch(
                cmd.expectedVersion(),
                category.getVersion());

        if (this.checkHasSoftDeletedProductsPort.hasSoftDeletedProduct(categoryId)) {
            throw new BusinessRuleException("Cannot delete category with existing products");
        }

        this.deletePort.deleteById(categoryId);

        final var event = new CategoryHardDeletedEvent(categoryId);
        this.eventPort.publishEvent(event);

        this.imageDeleter.deleteQuietly(category.getImageKey());
    }
}
