package vn.edu.uit.msshop.product.product.application.service.command;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.RemoveProductVariantForVariantCommand;
import vn.edu.uit.msshop.product.product.application.exception.ProductMustHaveAtLeastOneVariantException;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.port.in.command.RemoveProductVariantForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.event.PublishProductEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DecreaseAllProductSoldCountsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DecreaseAllProductStockCountsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.domain.event.ProductUpdated;
import vn.edu.uit.msshop.product.product.domain.model.Product;

@Service
@RequiredArgsConstructor
public class RemoveProductVariantForVariantService
    implements RemoveProductVariantForVariantUseCase {
  private final LoadProductPort loadPort;
  private final UpdateProductPort updatePort;
  private final DecreaseAllProductSoldCountsPort decreaseSoldPort;
  private final DecreaseAllProductStockCountsPort decreaseStockPort;
  private final PublishProductEventPort eventPort;

  @Override
  @Transactional
  public void removeVariant(RemoveProductVariantForVariantCommand command) {
    final var productId = command.id();
    final var product = this.loadPort.loadById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));

    if (product.getVariants().size() <= 1) {
      throw new ProductMustHaveAtLeastOneVariantException(productId);
    }

    final var newConfiguration = product.getConfiguration().removeVariant(command.variantId());

    final var next = new Product(product.getId(), product.getName(), product.getCategoryId(),
        product.getBrandId(), newConfiguration, product.getImageKeys(), product.getVersion(),
        product.getDeletionTime());
    final var saved = this.updatePort.update(next);

    if (command.soldDecrement() > 0) {
      this.decreaseSoldPort.decreaseAll(Map.of(productId, command.soldDecrement()));
    }
    if (command.stockDecrement() > 0) {
      this.decreaseStockPort.decreaseAll(Map.of(productId, command.stockDecrement()));
    }

    this.eventPort.publish(new ProductUpdated(saved.getId()));
  }
}
