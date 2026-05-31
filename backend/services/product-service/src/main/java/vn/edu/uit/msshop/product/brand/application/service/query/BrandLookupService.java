package vn.edu.uit.msshop.product.brand.application.service.query;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.BrandLookupUseCases;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.CheckBrandExistsPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.ListBrandsPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.ListSoftDeletedBrandsPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadSoftDeletedBrandPort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class BrandLookupService
        implements
        BrandLookupUseCases.ListActive,
        BrandLookupUseCases.ListSoftDeleted,
        BrandLookupUseCases.CheckExistsById,
        BrandLookupUseCases.FindActiveById,
        BrandLookupUseCases.FindActiveLogoById,
        BrandLookupUseCases.FindSoftDeletedById {

    private final ListBrandsPort listPort;
    private final ListSoftDeletedBrandsPort listSoftDeletedPort;
    private final CheckBrandExistsPort checkExistsPort;
    private final LoadBrandPort loadPort;
    private final LoadSoftDeletedBrandPort loadSoftDeletedPort;

    private final BrandViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.BRAND_LIST)
    public PageResponseDto<BrandView> listActive(
            PageRequestDto pageRequest) {
        final var page = listPort.list(pageRequest);
        return page.map(this.mapper::toView);
    }

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<BrandView> listSoftDeleted(
            PageRequestDto pageRequest) {
        var page = listSoftDeletedPort.listSoftDeleted(pageRequest);
        return page.map(this.mapper::toView);
    }

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsById(
            final BrandId id) {
        return this.checkExistsPort.existsById(id);
    }

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.BRAND,
            key = "#id.value()")
    public BrandView findActiveById(
            final BrandId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toView)
                .orElseThrow(() -> new BrandNotFoundException(id));
    }

    @Override
    @Transactional(
            readOnly = true)
    public BrandLogoView findActiveLogoById(
            final BrandId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toLogoView)
                .orElseThrow(() -> new BrandNotFoundException(id));
    }

    @Override
    @Transactional(
            readOnly = true)
    public BrandView findSoftDeletedById(
            final BrandId id) {
        return this.loadSoftDeletedPort.loadSoftDeletedById(id)
                .map(this.mapper::toView)
                .orElseThrow(() -> new BrandNotFoundException(id));
    }
}
