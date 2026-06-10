package vn.edu.uit.msshop.product.category.application.service.command.image;

import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.command.image.CategoryImageUpdateByIdCommand;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryImageKeyNotFoundException;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.command.image.CategoryImageUpdateByIdUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.CategoryEventPublicationPort;
import vn.edu.uit.msshop.product.category.application.port.out.image.CategoryImageStoragePort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.command.CategoryUpdatePort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.lookup.CategoryActiveLookupByIdPort;
import vn.edu.uit.msshop.product.category.application.service.command.support.CategoryVersionGuard;
import vn.edu.uit.msshop.product.category.domain.event.CategoryImageUpdatedEvent;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryVersion;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryImageUpdateByIdService
        implements CategoryImageUpdateByIdUseCase {

    private final CategoryActiveLookupByIdPort activeLookupByIdPort;
    private final CategoryUpdatePort updatePort;

    private final CategoryImageStoragePort imageStoragePort;
    private final CategoryImageDeleter imageDeleter;

    private final CategoryEventPublicationPort eventPublicationPort;
    private final CategoryViewMapper mapper;

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
    public CategoryImageView update(
            final CategoryImageUpdateByIdCommand cmd) {
        final var categoryId = new CategoryId(cmd.categoryId());
        final var newImageKey = new CategoryImageKey(cmd.categoryNewImageKey());
        final var expectedVersion = new CategoryVersion(cmd.categoryVersion());

        final var category = this.activeLookupByIdPort.loadActiveById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        CategoryVersionGuard.ensureMatch(
                expectedVersion,
                category.getVersion());

        final var saved = this.commitImageChange(category, newImageKey);
        if (saved == null) {
            return this.mapper.toImageView(category);
        }

        final var event = new CategoryImageUpdatedEvent(
                saved.getId(),
                saved.getImageKey(),
                category.getImageKey());
        this.eventPublicationPort.publishEvent(event);

        this.imageDeleter.deleteQuietly(category.getImageKey());

        return this.mapper.toImageView(saved);
    }

    private @Nullable Category commitImageChange(
            final Category current,
            final CategoryImageKey newImageKey) {
        if (newImageKey.equals(current.getImageKey())) {
            return null;
        }

        this.ensureImageKeyExistsInTemp(newImageKey);
        this.imageStoragePort.publishImage(newImageKey);

        try {
            final var next = current.changeImageKey(newImageKey);
            return this.updatePort.update(next);
        } catch (final RuntimeException e) {
            try {
                this.imageStoragePort.unpublishImage(newImageKey);
            } catch (final RuntimeException compensateEx) {
                e.addSuppressed(compensateEx);
                log.error("Compensation failed for key '{}'",
                        newImageKey.value(),
                        compensateEx);
            }
            throw e;
        }
    }

    private void ensureImageKeyExistsInTemp(
            final CategoryImageKey imageKey) {
        if (!this.imageStoragePort.existsAsTemp(imageKey)) {
            throw new CategoryImageKeyNotFoundException(imageKey);
        }
    }
}
