package vn.edu.uit.msshop.product.product.application.port.in.query;

import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface ListSoftDeletedProductsUseCase {
    PageResponseDto<ProductView> listSoftDeleted(
            final PageRequestDto pageRequest);
}
