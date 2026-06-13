package vn.edu.uit.msshop.product.variant.application.service.command.image;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.image.VariantImageDeletionByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.domain.event.VariantImageUpdatedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.command.image.VariantImageDeletionByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.application.service.command.support.VariantVersionGuard;

@Service
@RequiredArgsConstructor
@Slf4j
class VariantImageDeletionByIdService
        implements VariantImageDeletionByIdUseCase {

    private final LoadVariantPort loadPort;
    private final UpdateVariantPort updatePort;

    private final VariantImageDeleter imageDeleter;

    private final VariantEventPublicationPort eventPublicationPort;
    private final VariantViewMapper mapper;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT,
                            key = "#command.id().value()"),
                    @CacheEvict(
                            cacheNames = CacheNames.VARIANT_LIST,
                            allEntries = true)
            })
    public VariantImageView deleteImage(
            final VariantImageDeletionByIdCommand cmd) {
        final var variantId = new VariantId(cmd.variantId());
        final var expectedVersion = new VariantVersion(cmd.variantVersion());

        final var variant = this.loadPort.loadById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        final var oldKey = variant.getImageKey();
        if (oldKey == null) {
            return this.mapper.toImageView(variant);
        }

        VariantVersionGuard.ensureMatch(
                expectedVersion,
                variant.getVersion());

        final var next = new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getProductName(),
                variant.getPrice(),
                variant.getTraits(),
                variant.getTargets(),
                null,
                expectedVersion,
                variant.getDeletionTime());
        final var saved = this.updatePort.update(next);

        final var event = new VariantImageUpdatedEvent(
                saved.getId(),
                null,
                oldKey);
        this.eventPublicationPort.publishEvent(event);

        this.imageDeleter.deleteQuietly(oldKey);

        return this.mapper.toImageView(saved);
    }
}
