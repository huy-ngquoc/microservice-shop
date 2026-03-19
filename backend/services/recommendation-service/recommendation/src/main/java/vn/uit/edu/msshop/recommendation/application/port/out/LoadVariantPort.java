package vn.uit.edu.msshop.recommendation.application.port.out;

import java.util.List;

import vn.uit.edu.msshop.recommendation.domain.model.Variant;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantId;

public interface LoadVariantPort {
    public Variant loadById(VariantId id);
    public List<Variant> loadByTarget(String age, String gender, String shape, String bodyShape);
}
