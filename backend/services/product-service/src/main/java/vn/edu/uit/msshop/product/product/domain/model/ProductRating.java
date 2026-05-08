package vn.edu.uit.msshop.product.product.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingAmount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingAverage;
import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class ProductRating {
    private final ProductId id;

    private final ProductRatingAverage average;

    private final ProductRatingAmount amount;

    public ProductRating(
            final ProductId id,
            final ProductRatingAverage average,
            final ProductRatingAmount amount) {
        this.id = Domains.requireNonNull(id, "Product ID must NOT be null");
        this.average = Domains.requireNonNull(average, "Product rating average must NOT be null");
        this.amount = Domains.requireNonNull(amount, "Product rating amount must NOT be null");

        ProductRating.validate(this.average, this.amount);
    }

    public static ProductRating zero(
            final ProductId id) {
        final var average = new ProductRatingAverage(0);
        final var amount = new ProductRatingAmount(0);

        return new ProductRating(id, average, amount);
    }

    private static void validate(
            final ProductRatingAverage average,
            final ProductRatingAmount amount) {
        final var avgValue = average.value();
        final var amountValue = amount.value();

        if (amountValue > 0) {
            if (avgValue < 1) {
                throw new DomainException(
                        "Product rating average must NOT be less than 1 when there is a rating");
            }
        } else {
            if (Float.compare(avgValue, 0) != 0) {
                throw new DomainException("Product rating average must be 0 when there is NOT any rating");
            }
        }
    }
}
