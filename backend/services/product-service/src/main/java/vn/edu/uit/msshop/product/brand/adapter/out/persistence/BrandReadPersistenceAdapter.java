package vn.edu.uit.msshop.product.brand.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.existence.BrandActiveExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.listing.BrandActiveListingPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.listing.BrandSoftDeletedListingPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.lookup.BrandActiveLookupByIdPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.lookup.BrandSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.shared.adapter.out.persistence.PageRequests;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Component
@RequiredArgsConstructor
public class BrandReadPersistenceAdapter
        implements
        BrandActiveListingPort,
        BrandSoftDeletedListingPort,
        BrandActiveLookupByIdPort,
        BrandSoftDeletedLookupByIdPort,
        BrandActiveExistenceCheckByIdPort {

    private final BrandMongoRepository repository;
    private final BrandPersistenceMapper mapper;

    @Override
    public PageResponseDto<Brand> list(
            final PageRequestDto pageRequest) {
        final var pageable = PageRequests.toPageable(pageRequest, BrandDocument.Fields.id);
        final var page = this.repository.findAllByDeletionTimeIsNull(pageable);

        final var brands = page.getContent().stream()
                .map(this.mapper::toDomain)
                .toList();

        return new PageResponseDto<>(
                brands,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public PageResponseDto<Brand> listSoftDeleted(
            final PageRequestDto pageRequest) {
        final var pageable = PageRequests.toPageable(pageRequest, BrandDocument.Fields.id);
        final var page = this.repository.findAllByDeletionTimeIsNotNull(pageable);

        final var brands = page.getContent().stream()
                .map(this.mapper::toDomain)
                .toList();

        return new PageResponseDto<>(
                brands,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public boolean existsById(
            final BrandId id) {
        final var jpaId = id.value();
        return this.repository.existsByIdAndDeletionTimeIsNull(jpaId);
    }

    @Override
    public Optional<Brand> loadById(
            final BrandId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Optional<Brand> loadSoftDeletedById(
            final BrandId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNotNull(jpaId)
                .map(this.mapper::toDomain);
    }
}
