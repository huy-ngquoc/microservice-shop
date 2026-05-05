package vn.edu.uit.msshop.product.brand.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.brand.application.dto.command.DeleteBrandLogoCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.command.DeleteBrandLogoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.logo.BrandLogoStoragePort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandLogoUpdated;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteBrandLogoService implements DeleteBrandLogoUseCase {
  private final LoadBrandPort loadPort;
  private final UpdateBrandPort updatePort;
  private final BrandLogoStoragePort logoStoragePort;
  private final BrandViewMapper mapper;
  private final PublishBrandEventPort eventPort;

  @Override
  @Transactional
  public BrandLogoView deleteLogo(final DeleteBrandLogoCommand command) {
    final var brandId = command.id();
    final var brand =
        this.loadPort.loadById(brandId).orElseThrow(() -> new BrandNotFoundException(brandId));

    final var oldKey = brand.getLogoKey();
    if (oldKey == null) {
      return this.mapper.toLogoView(brand);
    }

    final var expectedVersion = command.expectedVersion();
    final var currentVersion = brand.getVersion();
    if (!expectedVersion.equals(currentVersion)) {
      throw new OptimisticLockException(expectedVersion.value(), currentVersion.value());
    }

    final var next =
        new Brand(brand.getId(), brand.getName(), null, expectedVersion, brand.getDeletionTime());
    final var saved = this.updatePort.update(next);

    final var event = new BrandLogoUpdated(saved.getId(), null, oldKey);
    this.eventPort.publish(event);

    this.deleteOldLogo(oldKey);

    return this.mapper.toLogoView(saved);
  }

  private void deleteOldLogo(final BrandLogoKey oldKey) {
    try {
      this.logoStoragePort.deleteLogo(oldKey);
    } catch (Exception e) {
      log.warn("Failed to delete old image key '{}', manual cleanup required", oldKey.value(), e);
    }
  }
}
