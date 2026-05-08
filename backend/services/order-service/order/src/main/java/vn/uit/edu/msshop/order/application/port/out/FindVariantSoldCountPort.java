package vn.uit.edu.msshop.order.application.port.out;

import java.util.List;
import java.util.Optional;

import vn.uit.edu.msshop.order.domain.model.VariantSoldCount;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;

public interface FindVariantSoldCountPort {
    public Optional<VariantSoldCount>  findById(VariantId id);
    public List<VariantSoldCount> findByIdInList(List<VariantId> ids);
    public List<VariantSoldCount> findAll();
}
