package vn.uit.edu.msshop.order.application.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.port.in.DeleteVariantSoldCountUseCase;
import vn.uit.edu.msshop.order.application.port.out.DeleteVariantSoldCountPort;
import vn.uit.edu.msshop.order.bootstrap.config.cache.CacheNames;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class DeleteVariantSoldCountService implements DeleteVariantSoldCountUseCase {
    private final DeleteVariantSoldCountPort deletePort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.VARIANT_SOLD_COUNT,
            key = "#id.value()")
    public void delete(
            VariantId id) {
        deletePort.deleteById(id);
    }

}
