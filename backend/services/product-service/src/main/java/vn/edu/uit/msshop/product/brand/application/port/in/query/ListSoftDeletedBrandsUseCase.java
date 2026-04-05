package vn.edu.uit.msshop.product.brand.application.port.in.query;

import vn.edu.uit.msshop.product.brand.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

public interface ListSoftDeletedBrandsUseCase {
    PageResponseDto<BrandView> listSoftDeleted(
            final PageRequestDto pageRequest);
}
