package vn.edu.uit.msshop.product.product.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingAmount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingTotal;
import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class ProductRating {
    private static final int MIN_POINT = 1;
    private static final int MAX_POINT = 5;

    private final ProductId id;
    private final ProductRatingTotal total;
    private final ProductRatingAmount amount;

    public ProductRating(
            final ProductId id,
            final ProductRatingTotal total,
            final ProductRatingAmount amount) {
        this.id = Domains.requireNonNull(id, "Product ID must NOT be null");
        this.total = Domains.requireNonNull(total, "Product rating total must NOT be null");
        this.amount = Domains.requireNonNull(amount, "Product rating amount must NOT be null");

        ProductRating.validate(this.total, this.amount);
    }

    public static ProductRating zero(
            final ProductId id) {
        final var total = new ProductRatingTotal(0);
        final var amount = new ProductRatingAmount(0);

        return new ProductRating(id, total, amount);
    }

    public double getAverageValue() {
        final var amountValue = amount.value();
        if (amountValue <= 0) {
            return 0;
        }

        final var totalValue = total.value();
        return (double) totalValue / amountValue;
    }

    private static void validate(
            final ProductRatingTotal total,
            final ProductRatingAmount amount) {
        final var totalValue = total.value();
        final var amountValue = amount.value();

        final var minValue = amountValue * MIN_POINT;
        if (totalValue < minValue) {
            throw new DomainException("Rating total below min possible");
        }

        final var maxValue = amountValue * MAX_POINT;
        if (totalValue > maxValue) {
            throw new DomainException("Rating total above max possible");
        }
    }
}
