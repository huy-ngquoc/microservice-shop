package vn.edu.uit.msshop.product.category.application.port.in.query;

import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

public interface ListCategoriesUseCase {
    PageResponseDto<CategoryView> list(
            final PageRequestDto pageRequest);
}
