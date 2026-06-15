package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import java.util.Collection;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.out.sync.response.VariantSoldCountResponse;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.VariantSoldCountBulkFetchPort;
import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantSoldCountSnapshot;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;

@Component
@RequiredArgsConstructor
public class VariantSoldCountFeignAdapter
        implements VariantSoldCountBulkFetchPort {

    private final VariantSoldCountFeignClient feignClient;

    @Override
    public Collection<VariantSoldCountSnapshot> fetchAll() {
        return this.feignClient.getSoldCounts().stream()
                .map(VariantSoldCountFeignAdapter::toDomainDto)
                .toList();
    }

    private static VariantSoldCountSnapshot toDomainDto(
            final VariantSoldCountResponse response) {
        return new VariantSoldCountSnapshot(
                new VariantId(response.variantId()),
                new VariantSoldCountValue(response.soldCount()));
    }
}
