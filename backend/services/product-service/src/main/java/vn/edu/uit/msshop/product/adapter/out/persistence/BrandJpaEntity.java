package vn.edu.uit.msshop.product.adapter.out.persistence;

import java.util.UUID;

import org.jspecify.annotations.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

@Entity
@Table(
        name = "Brands")
@Getter
@Setter
@NoArgsConstructor(
        access = AccessLevel.PACKAGE)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class BrandJpaEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(
            nullable = false,
            length = BrandName.MAX_LENGTH)
    private String name;

    // --- Logo (flatten from value-object Logo) ---

    private String logoUrl;

    private String logoKey;

    private Integer logoWidth;

    private Integer logoHeight;

    @Version
    private Long version;

    public static @NonNull BrandJpaEntity of(
            UUID id,
            String name,
            String logoUrl,
            String logoKey,
            Integer logoWidth,
            Integer logoHeight) {
        return new BrandJpaEntity(
                id,
                name,
                logoUrl,
                logoKey,
                logoWidth,
                logoHeight,
                null);
    }
}
