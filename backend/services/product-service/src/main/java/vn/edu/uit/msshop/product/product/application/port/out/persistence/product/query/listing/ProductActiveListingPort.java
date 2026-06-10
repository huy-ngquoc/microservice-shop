package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface ProductActiveListingPort {
    PageResponseDto<Product> list(
            final PageRequestDto pageRequest);
}
