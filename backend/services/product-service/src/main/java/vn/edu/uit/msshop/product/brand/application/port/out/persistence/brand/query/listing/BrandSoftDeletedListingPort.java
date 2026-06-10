package vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.listing;

import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface BrandSoftDeletedListingPort {
    PageResponseDto<Brand> listSoftDeleted(
            final PageRequestDto pageRequest);
}
