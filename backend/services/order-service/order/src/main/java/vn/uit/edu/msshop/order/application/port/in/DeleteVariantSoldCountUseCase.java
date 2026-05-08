package vn.uit.edu.msshop.order.application.port.in;

import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;

public interface DeleteVariantSoldCountUseCase {
    public void delete(VariantId id);
}
