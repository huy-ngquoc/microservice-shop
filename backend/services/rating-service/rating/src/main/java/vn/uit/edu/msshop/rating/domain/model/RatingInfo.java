package vn.uit.edu.msshop.rating.domain.model;

import java.time.Instant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;
import vn.uit.edu.msshop.rating.domain.model.valueobject.CreateAt;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingCount;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingTotal;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UpdateAt;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class RatingInfo {
    @EqualsAndHashCode.Include
    private final ProductId productId;

    private final RatingCount ratingCount;

    private final RatingTotal ratingTotal;

    private final CreateAt createAt;

    private final UpdateAt updateAt;

    public RatingInfo(
            ProductId productId,
            RatingCount ratingCount,
            RatingTotal totalPoint,
            CreateAt createAt,
            UpdateAt updateAt) {
        this.productId = Domains.requireNonNull(productId, "Product ID must not be null");
        this.ratingCount = Domains.requireNonNull(ratingCount, "Rating count must not be null");
        this.ratingTotal = Domains.requireNonNull(totalPoint, "Total point must not be null");
        this.createAt = Domains.requireNonNull(createAt, "Creation time must not be null");
        this.updateAt = Domains.requireNonNull(updateAt, "Update time must not be null");

        RatingInfo.validate(totalPoint, ratingCount);
    }

    public static RatingInfo newRating(
            final ProductId productId,
            final RatingPoint ratingPoint) {
        final var newCount = RatingCount.one();
        final var newTotal = new RatingTotal(ratingPoint.value());

        final var instantNow = Instant.now();
        final var createAt = new CreateAt(instantNow);
        final var updateAt = new UpdateAt(instantNow);

        return new RatingInfo(
                productId,
                newCount,
                newTotal,
                createAt,
                updateAt);
    }

    public RatingInfo addRating(
            final RatingPoint ratingPoint) {
        final var currentTotalValue = this.ratingTotal.value();
        final var incrementPointValue = ratingPoint.value();

        final var newTotalValue = currentTotalValue + incrementPointValue;
        final var newCountValue = this.ratingCount.value() + 1;

        final var newTotal = new RatingTotal(newTotalValue);
        final var newCount = new RatingCount(newCountValue);

        return new RatingInfo(
                this.productId,
                newCount,
                newTotal,
                createAt,
                new UpdateAt(Instant.now()));
    }

    public RatingInfo removeRating(
            final RatingPoint ratingPoint) {
        final var currentCountValue = this.ratingCount.value();
        if (currentCountValue <= 1) {
            return new RatingInfo(
                    this.productId,
                    RatingCount.zero(),
                    RatingTotal.zero(),
                    this.createAt,
                    new UpdateAt(Instant.now()));
        }

        final var currentTotalValue = this.ratingTotal.value();
        final var decrementPointValue = ratingPoint.value();

        final var newTotalValue = currentTotalValue - decrementPointValue;
        final var newCountValue = this.ratingCount.value() - 1;

        final var newTotal = new RatingTotal(newTotalValue);
        final var newCount = new RatingCount(newCountValue);

        return new RatingInfo(
                this.productId,
                newCount,
                newTotal,
                createAt,
                new UpdateAt(Instant.now()));
    }

    private static void validate(
            final RatingTotal total,
            final RatingCount count) {
        final var totalValue = total.value();
        final var countValue = count.value();

        final var minValue = countValue * RatingPoint.MIN_VALUE;
        if (totalValue < minValue) {
            throw new DomainException("Rating total below min possible");
        }

        final var maxValue = countValue * RatingPoint.MAX_VALUE;
        if (totalValue > maxValue) {
            throw new DomainException("Rating total above max possible");
        }
    }
}
