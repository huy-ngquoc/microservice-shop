package vn.uit.edu.msshop.order.application.port.in;

import java.util.List;

import vn.uit.edu.msshop.order.application.dto.query.VariantSoldCountView;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;

public interface FindVariantSoldCountUseCase {
    public VariantSoldCountView findById(VariantId id);
    public List<VariantSoldCountView> findByListId(List<VariantId> ids);
    public List<VariantSoldCountView> findAll();
}
