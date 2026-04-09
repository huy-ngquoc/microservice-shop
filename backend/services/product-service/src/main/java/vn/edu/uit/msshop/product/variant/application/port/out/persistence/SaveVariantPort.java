package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface SaveVariantPort {
    public Variant save(Variant v);
    public List<Variant> saveAll(List<Variant> variants);
}
