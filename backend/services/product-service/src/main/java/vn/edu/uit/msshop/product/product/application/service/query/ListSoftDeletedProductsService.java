package vn.edu.uit.msshop.product.product.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.ListSoftDeletedProductsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ListSoftDeletedProductsPort;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class ListSoftDeletedProductsService
        implements ListSoftDeletedProductsUseCase {
    private final ListSoftDeletedProductsPort listSoftDeletedPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<ProductView> listSoftDeleted(
            final PageRequestDto pageRequest) {
        final var page = this.listSoftDeletedPort.listSoftDeleted(pageRequest);
        return page.map(this.mapper::toView);
    }
}
