package vn.edu.uit.msshop.product.product.adapter.out.persistence.rating;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingAmount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingTotal;

@Component
public class ProductRatingPersistenceMapper {
    public ProductRating toDomain(
            final ProductRatingDocument entity) {
        final var id = new ProductId(entity.getId());

        final var total = new ProductRatingTotal(entity.getTotal());
        final var amount = new ProductRatingAmount(entity.getAmount());

        return new ProductRating(id, total, amount);
    }
}
