package vn.edu.uit.msshop.product.variant.application.service.command.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.variant.application.dto.command.data.NewVariantForNewProductData;
import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkCreationForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkCreationForNewProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantEventPublicationPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.CreateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.InitializeAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.InitializeAllVariantStockCountsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantCreatedEvent;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;

@Service
@RequiredArgsConstructor
class VariantBulkCreationForNewProductService
        implements VariantBulkCreationForNewProductUseCase {

    private final CreateAllVariantsPort createAllVariantsPort;
    private final InitializeAllVariantSoldCountsPort initializeAllSoldCountsPort;
    private final InitializeAllVariantStockCountsPort initializeAllStockCountsPort;
    private final VariantEventPublicationPort eventPublicationPort;
    private final VariantViewMapper mapper;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.VARIANT_LIST,
            allEntries = true)
    public List<VariantView> create(
            final VariantBulkCreationForNewProductCommand cmd) {
        final var saved = this.createVariants(cmd);
        final var soldCountByVariantId = this.initializeSoldCounts(saved);
        final var stockCountByVariantId = this.initializeStockCounts(saved);
        this.publishCreatedEvents(saved);
        return this.toViews(
                saved,
                soldCountByVariantId,
                stockCountByVariantId);
    }

    private List<Variant> createVariants(
            final VariantBulkCreationForNewProductCommand command) {
        final var productId = new VariantProductId(command.productId());
        final var productName = new VariantProductName(command.productName());

        final var newVariantDataList = command.newVariantList();
        final var amountVariant = newVariantDataList.size();

        final var newVariantList = new ArrayList<NewVariant>(amountVariant);
        for (final var data : newVariantDataList) {
            final var newVariant = VariantBulkCreationForNewProductService.toNewVariant(
                    productId,
                    productName,
                    data);
            newVariantList.add(newVariant);
        }
        return this.createAllVariantsPort.createAll(newVariantList);
    }

    private static NewVariant toNewVariant(
            final VariantProductId productId,
            final VariantProductName productName,
            final NewVariantForNewProductData data) {
        return new NewVariant(
                VariantId.newId(),
                productId,
                productName,
                new VariantPrice(data.price()),
                VariantTraits.of(data.traitList()),
                VariantTargets.of(data.targetList()));
    }

    private Map<VariantId, VariantSoldCount> initializeSoldCounts(
            final List<Variant> saved) {
        final var newSoldCountList = new ArrayList<NewVariantSoldCount>(saved.size());
        for (final var variant : saved) {
            final var newSoldCount = new NewVariantSoldCount(
                    variant.getId(),
                    variant.getProductId());
            newSoldCountList.add(newSoldCount);
        }
        return this.initializeAllSoldCountsPort.initializeAll(newSoldCountList);
    }

    private Map<VariantId, VariantStockCount> initializeStockCounts(
            final List<Variant> saved) {
        final var newStockCountList = new ArrayList<NewVariantStockCount>(saved.size());
        for (final var variant : saved) {
            final var newStockCount = new NewVariantStockCount(
                    variant.getId(),
                    variant.getProductId());
            newStockCountList.add(newStockCount);
        }
        return this.initializeAllStockCountsPort.initializeAll(newStockCountList);
    }

    private List<VariantView> toViews(
            final List<Variant> saved,
            final Map<VariantId, VariantSoldCount> soldCountByVariantId,
            final Map<VariantId, VariantStockCount> stockCountByVariantId) {
        final var viewList = new ArrayList<VariantView>(saved.size());
        for (final var variant : saved) {
            final var view = this.toView(
                    variant,
                    soldCountByVariantId,
                    stockCountByVariantId);
            viewList.add(view);
        }
        return viewList;
    }

    private VariantView toView(
            final Variant variant,
            final Map<VariantId, VariantSoldCount> soldCountByVariantId,
            final Map<VariantId, VariantStockCount> stockCountByVariantId) {
        final var soldCount = soldCountByVariantId.get(variant.getId());
        Objects.requireNonNull(
                soldCount,
                () -> "Sold count must be initialized for variant " + variant.getId());

        final var stockCount = stockCountByVariantId.get(variant.getId());
        Objects.requireNonNull(
                stockCount,
                () -> "Stock count must be initialized for variant " + variant.getId());

        return this.mapper.toView(
                variant,
                soldCount,
                stockCount);
    }

    private void publishCreatedEvents(
            final List<Variant> saved) {
        for (final var variant : saved) {
            final var event = new VariantCreatedEvent(variant.getId());
            this.eventPublicationPort.publishEvent(event);
        }
    }
}
