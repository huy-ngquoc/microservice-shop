package vn.edu.uit.msshop.product.category.application.service.query;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.query.ListCategoriesUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.ListCategoriesPort;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class ListCategoriesService implements ListCategoriesUseCase {
    private final ListCategoriesPort listPort;
    private final CategoryViewMapper mapper;

    @Override
    public PageResponseDto<CategoryView> list(
            final PageRequestDto pageRequest) {
        final var page = this.listPort.list(pageRequest);
        return page.map(this.mapper::toView);
    }
}
