package vn.edu.uit.msshop.product.category.application.port.in.query;

import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

public interface ListSoftDeletedCategoriesUseCase {
    PageResponseDto<CategoryView> listSoftDeleted(
            final PageRequestDto pageRequest);
}
