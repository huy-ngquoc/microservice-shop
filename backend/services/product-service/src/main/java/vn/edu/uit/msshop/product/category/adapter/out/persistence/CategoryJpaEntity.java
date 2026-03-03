package vn.edu.uit.msshop.product.category.adapter.out.persistence;

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
import vn.edu.uit.msshop.product.category.domain.model.CategoryName;

@Entity
@Table(
        name = "Categories")
@Getter
@Setter
@NoArgsConstructor(
        access = AccessLevel.PACKAGE)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class CategoryJpaEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(
            nullable = false,
            length = CategoryName.MAX_LENGTH)
    private String name;

    // --- Image (flatten from value-object Image) ---

    private String imageUrl;

    private String imageKey;

    private Integer imageWidth;

    private Integer imageHeight;

    @Version
    private Long version;

    public static @NonNull CategoryJpaEntity of(
            final UUID id,
            final String name,
            final String imageUrl,
            final String imageKey,
            final Integer imageWidth,
            final Integer imageHeight) {
        return new CategoryJpaEntity(
                id,
                name,
                imageUrl,
                imageKey,
                imageWidth,
                imageHeight,
                null);
    }
}
