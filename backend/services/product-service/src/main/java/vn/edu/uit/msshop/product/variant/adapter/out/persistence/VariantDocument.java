package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("Variants")
@Getter
@Setter
@SuppressWarnings("NullAway.Init")
@NoArgsConstructor(
        access = AccessLevel.PACKAGE)
@AllArgsConstructor(
        access = AccessLevel.PRIVATE)
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class VariantDocument {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private UUID productId;

    private String imageKey;

    private int price;

    private int sold;

    private List<String> traits;

    @Version
    @Nullable
    private Long version;

    public VariantDocument(
            UUID id,
            UUID productId,
            String imageKey,
            int price,
            int sold,
            List<String> traits) {
        this(
                id,
                productId,
                imageKey,
                price,
                sold,
                List.copyOf(traits),
                null);
    }

}
