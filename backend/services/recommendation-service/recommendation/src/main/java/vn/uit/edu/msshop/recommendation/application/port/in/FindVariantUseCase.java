package vn.uit.edu.msshop.recommendation.application.port.in;

import java.util.List;

import vn.uit.edu.msshop.recommendation.application.dto.query.VariantView;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantId;

public interface FindVariantUseCase {
    public VariantView findById(VariantId id);
    public List<VariantView> findByTarget(String age, String gender, String shape, String bodyShape);
}
