package vn.edu.uit.msshop.product.brand.application.port.out.persistence;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

public interface ListBrandsPort {
    PageResponseDto<Brand> list(
            final PageRequestDto pageRequest);
}
