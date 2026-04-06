package vn.edu.uit.msshop.product.category.application.service.query;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.query.ListSoftDeletedCategoriesUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.ListSoftDeletedCategoriesPort;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class ListSoftDeletedCategoriesService implements ListSoftDeletedCategoriesUseCase {
    private final ListSoftDeletedCategoriesPort listSoftDeletedPort;
    private final CategoryViewMapper mapper;

    @Override
    public PageResponseDto<CategoryView> listSoftDeleted(
            final PageRequestDto pageRequest) {
        final var page = this.listSoftDeletedPort.listSoftDeleted(pageRequest);
        return page.map(this.mapper::toView);
    }
}
