package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.PersistenceCreator;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductVariantDocument {
  private final UUID id;

  private final long price;

  private final List<String> traits;

  @PersistenceCreator
  public ProductVariantDocument(final UUID id, final long price, final List<String> traits) {
    this.id = id;
    this.price = price;
    this.traits = List.copyOf(traits);
  }
}
