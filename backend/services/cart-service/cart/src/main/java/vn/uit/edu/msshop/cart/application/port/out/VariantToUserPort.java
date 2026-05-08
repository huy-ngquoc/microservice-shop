package vn.uit.edu.msshop.cart.application.port.out;

import java.util.Set;

import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;

public interface VariantToUserPort {
    public void addMapping(VariantId variantId, UserId userId);
    public void removeMapping(VariantId variantId, UserId userId);
    public Set<String> getByVariantId(VariantId id);
}
