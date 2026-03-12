package vn.edu.uit.msshop.product.variant.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.annotation.Id;
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

    private VariantImageDocument image;

    private int price;

    private int sold;

}
