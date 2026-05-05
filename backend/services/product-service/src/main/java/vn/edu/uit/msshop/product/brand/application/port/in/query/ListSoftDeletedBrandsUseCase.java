package vn.edu.uit.msshop.product.brand.application.port.in.query;

import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

public interface ListSoftDeletedBrandsUseCase {
  PageResponseDto<BrandView> listSoftDeleted(final PageRequestDto pageRequest);
}
