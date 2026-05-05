package vn.edu.uit.msshop.product.variant.application.port.in.query;

import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.edu.uit.msshop.product.variant.application.dto.query.ListVariantsQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;

public interface ListVariantsUseCase {
    PageResponseDto<VariantView> list(
            final ListVariantsQuery query);
}
