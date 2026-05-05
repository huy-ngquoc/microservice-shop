package vn.edu.uit.msshop.product.category.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.query.FindSoftDeletedCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadSoftDeletedCategoryPort;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

@Service
@RequiredArgsConstructor
public class FindSoftDeletedCategoryService implements FindSoftDeletedCategoryUseCase {
  private final LoadSoftDeletedCategoryPort loadSoftDeletedPort;
  private final CategoryViewMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public CategoryView findSoftDeletedById(final CategoryId id) {
    return this.loadSoftDeletedPort.loadSoftDeletedById(id).map(this.mapper::toView)
        .orElseThrow(() -> new CategoryNotFoundException(id));
  }
}
