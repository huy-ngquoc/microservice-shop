package vn.edu.uit.msshop.product.category.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.query.FindCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

@Service
@RequiredArgsConstructor
public class FindCategoryService implements FindCategoryUseCase {
  private final LoadCategoryPort loadPort;
  private final CategoryViewMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public CategoryView findById(final CategoryId id) {
    return this.loadPort.loadById(id).map(this.mapper::toView)
        .orElseThrow(() -> new CategoryNotFoundException(id));
  }
}
