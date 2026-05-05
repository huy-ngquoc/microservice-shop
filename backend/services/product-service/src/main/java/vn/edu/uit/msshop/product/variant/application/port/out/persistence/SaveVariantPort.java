package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.List;

import org.springframework.lang.NonNull;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface SaveVariantPort {
  @NonNull
  public Variant save(Variant v);

  @NonNull
  public List<Variant> saveAll(List<Variant> variants);
}
