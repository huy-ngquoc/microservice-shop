package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.out.persistence.mapper.VariantPersistenceMapper;
import vn.edu.uit.msshop.product.variant.application.dto.query.ListVariantsQuery;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.ListVariantsPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTarget;
import vn.edu.uit.msshop.shared.adapter.out.persistence.PageRequests;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Component
@RequiredArgsConstructor
public class VariantListPersistenceAdapter
        implements
        ListVariantsPort {
    private final VariantMongoRepository repository;
    private final VariantPersistenceMapper mapper;

    @Override
    public PageResponseDto<Variant> list(
            final ListVariantsQuery query) {
        final var pageable = PageRequests.toPageable(
                query.pageRequest(),
                VariantDocument.Fields.id);

        final Page<VariantDocument> page;
        if (query.targets().isEmpty()) {
            page = this.repository.findAllByDeletionTimeIsNull(pageable);
        } else {
            final var rawTargets = query.targets().stream()
                    .map(VariantTarget::value)
                    .toList();
            page = this.repository.findAllByTargetsInAndDeletionTimeIsNull(rawTargets, pageable);
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
}
