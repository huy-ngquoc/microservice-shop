package vn.edu.uit.msshop.product.product.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckSoftDeletedProductExistsByCategoryUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CheckSoftDeletedProductExistsByCategoryPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckSoftDeletedProductExistsByCategoryService
    implements CheckSoftDeletedProductExistsByCategoryUseCase {
  private final CheckSoftDeletedProductExistsByCategoryPort checkPort;

  @Override
  @Transactional(readOnly = true)
  public boolean existsSoftDeletedByCategoryId(ProductCategoryId categoryId) {
    return this.checkPort.existsSoftDeletedByCategoryId(categoryId);
  }

}
