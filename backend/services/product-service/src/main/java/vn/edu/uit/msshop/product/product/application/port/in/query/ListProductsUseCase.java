package vn.edu.uit.msshop.product.product.application.port.in.query;

import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

public interface ListProductsUseCase {
    PageResponseDto<ProductView> list(
            final PageRequestDto pageRequest);
}
