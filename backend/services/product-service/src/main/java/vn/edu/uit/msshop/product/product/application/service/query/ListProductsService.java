package vn.edu.uit.msshop.product.product.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.ListProductsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ListProductsPort;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class ListProductsService implements ListProductsUseCase {
    private final ListProductsPort listPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<ProductView> list(
            PageRequestDto pageRequest) {
        final var page = this.listPort.list(pageRequest);
        return page.map(this.mapper::toView);
    }
}
