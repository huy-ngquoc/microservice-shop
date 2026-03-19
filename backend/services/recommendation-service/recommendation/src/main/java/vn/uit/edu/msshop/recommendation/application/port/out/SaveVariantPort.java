package vn.uit.edu.msshop.recommendation.application.port.out;

import vn.uit.edu.msshop.recommendation.domain.model.Variant;

public interface SaveVariantPort {
    public Variant save(Variant variant);
}
