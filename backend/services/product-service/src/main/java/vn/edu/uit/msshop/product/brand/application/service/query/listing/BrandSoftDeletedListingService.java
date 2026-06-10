package vn.edu.uit.msshop.product.brand.application.service.query.listing;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.query.listing.BrandSoftDeletedListingQuery;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.listing.BrandSoftDeletedListingUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.listing.BrandSoftDeletedListingPort;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class BrandSoftDeletedListingService
        implements BrandSoftDeletedListingUseCase {

    private final BrandSoftDeletedListingPort listSoftDeletedPort;
    private final BrandViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<BrandView> list(
            final BrandSoftDeletedListingQuery query) {
        var page = listSoftDeletedPort.listSoftDeleted(query.pageRequest());
        return page.map(this.mapper::toView);
    }
}
