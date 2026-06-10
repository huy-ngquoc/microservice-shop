package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.out.persistence.mapper.ProductPersistenceMapper;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing.ProductActiveListingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing.ProductSoftDeletedListingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveBulkLookupByIds;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.adapter.out.persistence.PageRequests;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Component
@RequiredArgsConstructor
public class ProductWritePersistenceAdapter
        implements
        ProductActiveListingPort,
        ProductSoftDeletedListingPort,
        ProductActiveLookupByIdPort,
        ProductActiveBulkLookupByIds,
        ProductSoftDeletedLookupByIdPort {
    private final ProductMongoRepository repository;
    private final ProductPersistenceMapper mapper;

    @Override
    public PageResponseDto<Product> list(
            final PageRequestDto pageRequest) {
        final var pageable = PageRequests.toPageable(pageRequest, ProductDocument.Fields.id);
        final var page = this.repository.findAllByDeletionTimeIsNull(pageable);

        final var products = page.getContent().stream().map(this.mapper::toDomain).toList();

        return new PageResponseDto<>(
                products,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public PageResponseDto<Product> listSoftDeleted(
            final PageRequestDto pageRequest) {
        final var pageable = PageRequests.toPageable(pageRequest, ProductDocument.Fields.id);
        final var page = this.repository.findAllByDeletionTimeIsNotNull(pageable);

        final var products = page.getContent().stream()
                .map(this.mapper::toDomain)
                .toList();

        return new PageResponseDto<>(
                products,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public Optional<Product> loadById(
            final ProductId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public List<Product> loadAllByIds(
            final Collection<ProductId> ids) {
        final var jpaIds = ids.stream().map(ProductId::value).toList();
        return this.repository.findAllByDeletionTimeIsNull(jpaIds).stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Product> loadSoftDeletedById(
            final ProductId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNotNull(jpaId)
                .map(this.mapper::toDomain);
    }
}
