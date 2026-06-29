package vn.edu.uit.msshop.product.variant.adapter.out.persistence.variant;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveBulkLookupByIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveBulkLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveCountByProductIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveListingPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantActiveLookupByIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantBulkLookupByProductIdPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantSoftDeletedBulkLookupByIdsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantSoftDeletedListingPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.query.VariantSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.shared.adapter.out.persistence.PageRequests;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Component
@RequiredArgsConstructor
public class VariantQueryPersistenceAdapter
        implements
        VariantActiveListingPort,
        VariantSoftDeletedListingPort,
        VariantActiveCountByProductIdPort,
        VariantActiveLookupByIdPort,
        VariantSoftDeletedLookupByIdPort,
        VariantActiveBulkLookupByIdsPort,
        VariantBulkLookupByProductIdPort,
        VariantActiveBulkLookupByProductIdPort,
        VariantSoftDeletedBulkLookupByIdsPort {

    private final VariantMongoRepository repository;
    private final VariantPersistenceMapper mapper;

    @Override
    public PageResponseDto<Variant> listActive(
            final PageRequestDto pageRequest,
            final VariantTargets targets) {
        final var pageable = PageRequests.toPageable(
                pageRequest,
                VariantDocument.Fields.id);
        final var targetList = targets.unwrap();

        final Page<VariantDocument> page;
        if (targetList.isEmpty()) {
            page = this.repository.findAllByDeletionTimeIsNull(pageable);
        } else {
            page = this.repository.findAllByTargetsInAndDeletionTimeIsNull(
                    targetList,
                    pageable);
        }

        final var variants = page.getContent().stream()
                .map(this.mapper::toDomain)
                .toList();

        return new PageResponseDto<>(
                variants,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public PageResponseDto<Variant> listSoftDeleted(
            final PageRequestDto pageRequest,

            @Nullable
            final VariantProductId productId) {
        final var pageable = PageRequests.toPageable(
                pageRequest,
                VariantDocument.Fields.id);

        final Page<VariantDocument> page;
        if (productId == null) {
            page = this.repository.findAllByDeletionTimeIsNotNull(pageable);
        } else {
            page = this.repository.findAllByProductIdAndDeletionTimeIsNotNull(
                    productId.value(),
                    pageable);
        }

        final var variants = page.getContent().stream()
                .map(this.mapper::toDomain)
                .toList();

        return new PageResponseDto<>(
                variants,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public long countActiveByProductId(
            final VariantProductId productId) {
        final var jpaProductId = productId.value();
        return this.repository.countByProductIdAndDeletionTimeIsNull(jpaProductId);
    }

    @Override
    public Optional<Variant> loadActiveById(
            final VariantId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Optional<Variant> loadSoftDeletedById(
            final VariantId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNotNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Map<VariantId, Variant> loadAllByIds(
            final Set<VariantId> ids) {
        final var jpaIds = ids.stream()
                .map(VariantId::value)
                .toList();

        final var docs = this.repository.findAllByIdInAndDeletionTimeIsNull(jpaIds);
        return docs.stream()
                .map(this.mapper::toDomain)
                .collect(Collectors.toUnmodifiableMap(
                        Variant::getId,
                        Function.identity()));
    }

    @Override
    public List<Variant> loadAllByProductId(
            final VariantProductId productId) {
        final var jpaProductId = productId.value();
        return this.repository.findAllByProductId(jpaProductId).stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public List<Variant> loadAllActiveByProductId(
            final VariantProductId productId) {
        final var jpaProductId = productId.value();
        return this.repository.findAllByProductIdAndDeletionTimeIsNull(jpaProductId).stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public Map<VariantId, Variant> loadAllSoftDeletedByIds(
            final Set<VariantId> idSet) {
        final var jpaIdList = idSet.stream()
                .map(VariantId::value)
                .toList();
        final var docs = this.repository.findAllByIdInAndDeletionTimeIsNotNull(jpaIdList);
        return docs.stream()
                .map(this.mapper::toDomain)
                .collect(Collectors.toUnmodifiableMap(
                        Variant::getId,
                        Function.identity()));
    }
}
