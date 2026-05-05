package vn.edu.uit.msshop.product.product.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckProductExistsByBrandUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CheckProductExistsByBrandPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

@Service
@RequiredArgsConstructor
public class CheckProductExistsByBrandService implements CheckProductExistsByBrandUseCase {
  private final CheckProductExistsByBrandPort checkExistsByBrandPort;

  @Override
  @Transactional(readOnly = true)
  public boolean existsByBrandId(final ProductBrandId brandId) {
    return this.checkExistsByBrandPort.existsByBrandId(brandId);
  }
}
