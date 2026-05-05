package vn.edu.uit.msshop.product.product.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckProductExistsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CheckProductExistsPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class CheckProductExistsService implements CheckProductExistsUseCase {
  private final CheckProductExistsPort checkExistsPort;

  @Override
  @Transactional(readOnly = true)
  public boolean existsById(final ProductId id) {
    return this.checkExistsPort.existsById(id);
  }
}
