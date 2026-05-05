package vn.edu.uit.msshop.product.category.application.port.out.persistence;

import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface ListCategoriesPort {
  PageResponseDto<Category> list(final PageRequestDto pageRequest);
}
