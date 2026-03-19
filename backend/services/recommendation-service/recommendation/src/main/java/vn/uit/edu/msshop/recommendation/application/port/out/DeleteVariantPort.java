package vn.uit.edu.msshop.recommendation.application.port.out;

import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantId;

public interface DeleteVariantPort {
    public void deleteById(VariantId id);
}
