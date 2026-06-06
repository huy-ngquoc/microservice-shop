package vn.edu.uit.msshop.product.brand.application.service.query.listing;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.BrandLookupUseCases;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.ListSoftDeletedBrandsPort;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class BrandSoftDeletedListingService
        implements BrandLookupUseCases.ListSoftDeleted {

    private final ListSoftDeletedBrandsPort listSoftDeletedPort;
    private final BrandViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<BrandView> listSoftDeleted(
            final PageRequestDto pageRequest) {
        var page = listSoftDeletedPort.listSoftDeleted(pageRequest);
        return page.map(this.mapper::toView);
    }
}
