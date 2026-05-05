package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantsForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.application.mapper.VariantViewMapper;
import vn.edu.uit.msshop.product.variant.application.port.in.command.CreateVariantsForNewProductUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.CreateAllVariantsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.InitializeAllVariantSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.InitializeAllVariantStockCountsPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantCreated;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantForNewProduct;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductName;

@Service
@RequiredArgsConstructor
public class CreateVariantsForNewProductService implements CreateVariantsForNewProductUseCase {
  private final CreateAllVariantsPort createAllVariantsPort;
  private final InitializeAllVariantSoldCountsPort initializeAllSoldCountsPort;
  private final InitializeAllVariantStockCountsPort initializeAllStockCountsPort;
  private final PublishVariantEventPort eventPort;
  private final VariantViewMapper mapper;

  @Override
  @Transactional
  public List<VariantView> create(final CreateVariantsForNewProductCommand command) {
    final var saved = this.createVariants(command);
    final var soldCountByVariantId = this.initializeSoldCounts(saved);
    final var stockCountByVariantId = this.initializeStockCounts(saved);
    this.publishCreatedEvents(saved);
    return this.toViews(saved, soldCountByVariantId, stockCountByVariantId);
  }

  private static NewVariant toNewVariant(final VariantProductId productId,
      final VariantProductName productName, final NewVariantForNewProduct newInputs) {
    return new NewVariant(VariantId.newId(), productId, productName, newInputs.price(),
        newInputs.traits(), newInputs.targets());
  }

  private List<Variant> createVariants(final CreateVariantsForNewProductCommand command) {
    final var newVariants = command.newVariantsForNewProduct().values().stream()
        .map(v -> CreateVariantsForNewProductService.toNewVariant(command.productId(),
            command.productName(), v))
        .toList();
    return this.createAllVariantsPort.createAll(newVariants);
  }

  private Map<VariantId, VariantSoldCount> initializeSoldCounts(final List<Variant> saved) {
    final var newSoldCounts =
        saved.stream().map(v -> new NewVariantSoldCount(v.getId(), v.getProductId())).toList();
    return this.initializeAllSoldCountsPort.initializeAll(newSoldCounts);
  }

  private Map<VariantId, VariantStockCount> initializeStockCounts(final List<Variant> saved) {
    final var newStockCounts =
        saved.stream().map(v -> new NewVariantStockCount(v.getId(), v.getProductId())).toList();
    return this.initializeAllStockCountsPort.initializeAll(newStockCounts);
  }

  private List<VariantView> toViews(final List<Variant> saved,
      final Map<VariantId, VariantSoldCount> soldCountByVariantId,
      final Map<VariantId, VariantStockCount> stockCountByVariantId) {
    return saved.stream().map(v -> this.toView(v, soldCountByVariantId, stockCountByVariantId))
        .toList();
  }

  private VariantView toView(final Variant variant,
      final Map<VariantId, VariantSoldCount> soldCountByVariantId,
      final Map<VariantId, VariantStockCount> stockCountByVariantId) {
    final var soldCount = soldCountByVariantId.get(variant.getId());
    Objects.requireNonNull(soldCount,
        () -> "Sold count must be initialized for variant " + variant.getId());

    final var stockCount = stockCountByVariantId.get(variant.getId());
    Objects.requireNonNull(stockCount,
        () -> "Stock count must be initialized for variant " + variant.getId());

    return this.mapper.toView(variant, soldCount, stockCount);
  }

  private void publishCreatedEvents(final List<Variant> saved) {
    for (final var variant : saved) {
      this.eventPort.publish(new VariantCreated(variant.getId()));
    }
  }
}
