package vn.edu.uit.msshop.product.brand.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.command.SoftDeleteBrandCommand;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.port.in.command.SoftDeleteBrandUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.validation.CheckBrandHasProductsPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandSoftDeleted;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandDeletionTime;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class SoftDeleteBrandService implements SoftDeleteBrandUseCase {
  private final LoadBrandPort loadPort;
  private final UpdateBrandPort updatePort;
  private final CheckBrandHasProductsPort checkHasProductsPort;
  private final PublishBrandEventPort eventPort;

  @Override
  @Transactional
  public void delete(SoftDeleteBrandCommand command) {
    final var brandId = command.id();
    final var brand =
        this.loadPort.loadById(brandId).orElseThrow(() -> new BrandNotFoundException(brandId));

    final var expectedVersion = command.expectedVersion();
    final var currentVersion = brand.getVersion();
    if (!expectedVersion.equals(currentVersion)) {
      throw new OptimisticLockException(expectedVersion.value(), currentVersion.value());
    }

    if (this.checkHasProductsPort.hasProducts(brandId)) {
      throw new BusinessRuleException("Cannot delete brand with existing products");
    }

    final var next = new Brand(brand.getId(), brand.getName(), brand.getLogoKey(),
        brand.getVersion(), BrandDeletionTime.now());

    final var saved = this.updatePort.update(next);
    this.eventPort.publish(new BrandSoftDeleted(saved.getId()));
  }
}
