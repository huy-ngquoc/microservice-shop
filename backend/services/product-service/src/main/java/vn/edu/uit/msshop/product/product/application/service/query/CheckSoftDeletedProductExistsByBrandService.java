package vn.edu.uit.msshop.product.product.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckSoftDeletedProductExistsByBrandUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CheckSoftDeletedProductExistsByBrandPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckSoftDeletedProductExistsByBrandService
    implements CheckSoftDeletedProductExistsByBrandUseCase {
  private final CheckSoftDeletedProductExistsByBrandPort checkPort;

  @Override
  @Transactional(readOnly = true)
  public boolean existsSoftDeletedByBrandId(final ProductBrandId brandId) {
    return this.checkPort.existsSoftDeletedByBrandId(brandId);
  }
}
