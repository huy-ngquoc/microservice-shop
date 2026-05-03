package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import java.util.Collection;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.VariantSoldCountResponse;
import vn.edu.uit.msshop.product.product.adapter.out.sync.SoldCountFetcher;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.FetchAllOrderSoldCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantOrderSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantSoldCountValue;

@Component
@RequiredArgsConstructor
public class OrderSoldCountFeignAdapter implements FetchAllOrderSoldCountsPort {
    private final SoldCountFetcher feignClient;

    @Override
    public Collection<VariantOrderSoldCount> fetchAll() {
        return this.feignClient.getSoldCounts().stream()
                .map(OrderSoldCountFeignAdapter::toDomainDto)
                .toList();
    }

    private static VariantOrderSoldCount toDomainDto(
            final VariantSoldCountResponse response) {
        return new VariantOrderSoldCount(
                new VariantId(response.getVariantId()),
                new VariantSoldCountValue(response.getSoldCount()));
    }
}
