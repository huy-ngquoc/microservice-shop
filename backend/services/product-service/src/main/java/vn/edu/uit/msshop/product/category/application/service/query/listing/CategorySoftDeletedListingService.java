package vn.edu.uit.msshop.product.category.application.service.query.listing;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.query.listing.CategorySoftDeletedListingQuery;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.query.listing.CategorySoftDeletedListingUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.listing.CategorySoftDeletedListingPort;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class CategorySoftDeletedListingService
        implements CategorySoftDeletedListingUseCase {

    private final CategorySoftDeletedListingPort listSoftDeletedPort;
    private final CategoryViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<CategoryView> list(
            final CategorySoftDeletedListingQuery query) {
        final var page = this.listSoftDeletedPort.listSoftDeleted(query.pageRequest());
        return page.map(this.mapper::toView);
    }
}
