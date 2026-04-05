package vn.edu.uit.msshop.product.brand.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.ListSoftDeletedBrandsUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.ListSoftDeletedBrandsPort;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class ListSoftDeletedBrandsService implements ListSoftDeletedBrandsUseCase {
    private final ListSoftDeletedBrandsPort listSoftDeletedPort;
    private final BrandViewMapper viewMapper;

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<BrandView> listSoftDeleted(
            PageRequestDto pageRequest) {
        var page = listSoftDeletedPort.listSoftDeleted(pageRequest);
        return page.map(this.viewMapper::toView);
    }
}
