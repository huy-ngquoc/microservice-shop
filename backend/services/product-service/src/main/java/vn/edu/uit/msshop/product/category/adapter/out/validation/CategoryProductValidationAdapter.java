package vn.edu.uit.msshop.product.category.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.port.out.validation.CheckCategoryHasProductsPort;
import vn.edu.uit.msshop.product.category.application.port.out.validation.CheckCategoryHasSoftDeletedProductsPort;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckProductExistsByCategoryUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckSoftDeletedProductExistsByCategoryUseCase;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;

@Component
@RequiredArgsConstructor
public class CategoryProductValidationAdapter
    implements CheckCategoryHasProductsPort, CheckCategoryHasSoftDeletedProductsPort {
  private final CheckProductExistsByCategoryUseCase checkProductExistsByCategoryUseCase;
  private final CheckSoftDeletedProductExistsByCategoryUseCase checkSoftDeletedProductExistsByCategoryUseCase;

  @Override
  public boolean hasProduct(final CategoryId id) {
    final var productCategoryId = new ProductCategoryId(id.value());
    return this.checkProductExistsByCategoryUseCase.existsByCategoryId(productCategoryId);
  }

  @Override
  public boolean hasSoftDeletedProduct(final CategoryId id) {
    final var productCategoryId = new ProductCategoryId(id.value());
    return this.checkSoftDeletedProductExistsByCategoryUseCase
        .existsSoftDeletedByCategoryId(productCategoryId);
  }
}
