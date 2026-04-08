package vn.uit.edu.msshop.order.application.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.port.in.DeleteVariantSoldCountUseCase;
import vn.uit.edu.msshop.order.application.port.out.DeleteVariantSoldCountPort;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;
@Component
@RequiredArgsConstructor
public class DeleteVariantSoldCountService implements DeleteVariantSoldCountUseCase {
    private final DeleteVariantSoldCountPort deletePort;

    @Override
    public void delete(VariantId id) {
        deletePort.deleteById(id);
    }

}
