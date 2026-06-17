package vn.edu.uit.msshop.product.product.adapter.out.persistence.product;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing.ProductActiveListingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing.ProductSoftDeletedListingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveBulkLookupByIdsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.adapter.out.persistence.PageRequests;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Component
@RequiredArgsConstructor
class ProductQueryPersistenceAdapter
        implements
        ProductActiveListingPort,
        ProductSoftDeletedListingPort,
        ProductActiveLookupByIdPort,
        ProductActiveBulkLookupByIdsPort,
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
    public Map<ProductId, Product> loadAllByIds(
            final Set<ProductId> idSet) {
        final var jpaIdSet = idSet.stream()
                .map(ProductId::value)
                .collect(Collectors.toUnmodifiableSet());
        final var docList = this.repository.findAllByDeletionTimeIsNull(jpaIdSet);

        return docList.stream()
                .map(this.mapper::toDomain)
                .collect(Collectors.toUnmodifiableMap(
                        Product::getId,
                        Function.identity()));
    }

    @Override
    public Optional<Product> loadSoftDeletedById(
            final ProductId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNotNull(jpaId)
                .map(this.mapper::toDomain);
    }
}
