package vn.edu.uit.msshop.product.category.application.service.command.image;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.command.CategoryImageLifecycleCommands;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.command.image.CategoryImageDeletionByIdUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.application.service.command.support.CategoryVersionGuard;
import vn.edu.uit.msshop.product.category.domain.event.CategoryImageUpdatedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryImageDeletionByIdService
        implements CategoryImageDeletionByIdUseCase {

    private final LoadCategoryPort loadPort;
    private final UpdateCategoryPort updatePort;

    private final CategoryImageDeleter imageDeleter;

    private final CategoryEventPublicationPort eventPublicationPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.CATEGORY,
                            key = "#cmd.id().value()"),
                    @CacheEvict(
                            cacheNames = CacheNames.CATEGORY_LIST,
                            allEntries = true)
            })
    public void delete(
            final CategoryImageLifecycleCommands.Delete cmd) {
        final var categoryId = cmd.id();
        final var category = this.loadPort.loadById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        final var oldKey = category.getImageKey();
        if (oldKey == null) {
            return;
        }

        CategoryVersionGuard.ensureMatch(
                cmd.expectedVersion(),
                category.getVersion());

        final var next = category.removeImageKey();
        final var saved = this.updatePort.update(next);

        final var event = new CategoryImageUpdatedEvent(
                saved.getId(),
                null,
                oldKey);
        this.eventPublicationPort.publishEvent(event);

        this.imageDeleter.deleteQuietly(oldKey);
    }
}
