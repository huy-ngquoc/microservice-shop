package vn.edu.uit.msshop.product.brand.domain.model;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandDeletionTime;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;
import vn.edu.uit.msshop.shared.domain.Domains;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldNameConstants
public final class Brand {
  @EqualsAndHashCode.Include
  private final BrandId id;

  private final BrandName name;

  @Nullable
  private final BrandLogoKey logoKey;

  // ===== Metadata =====

  private final BrandVersion version;

  @Nullable
  private final BrandDeletionTime deletionTime;

  public Brand(final BrandId id,

      final BrandName name,

      @Nullable final BrandLogoKey logoKey,

      final BrandVersion version,

      @Nullable final BrandDeletionTime deletionTime) {
    this.id = Domains.requireNonNull(id, "Id must NOT be null");
    this.name = Domains.requireNonNull(name, "Name must NOT be null");
    this.logoKey = logoKey;

    this.version = Domains.requireNonNull(version, "Version must NOT be null");
    this.deletionTime = deletionTime;
  }
}
