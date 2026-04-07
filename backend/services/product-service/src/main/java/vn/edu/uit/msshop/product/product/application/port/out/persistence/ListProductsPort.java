package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

public interface ListProductsPort {
    PageResponseDto<Product> list(
            final PageRequestDto pageRequest);
}
