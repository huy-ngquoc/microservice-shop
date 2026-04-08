package vn.uit.edu.msshop.order.application.port.out;

import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;

public interface DeleteVariantSoldCountPort {
    public void deleteById(VariantId id);
}
