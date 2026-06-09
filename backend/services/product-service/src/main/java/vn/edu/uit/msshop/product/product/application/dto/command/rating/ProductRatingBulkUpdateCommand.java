package vn.edu.uit.msshop.product.product.application.dto.command.rating;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.product.domain.sync.ProductRatingSnapshot;

public record ProductRatingBulkUpdateCommand(
        Collection<ProductRatingSnapshot> ratings) {
    public ProductRatingBulkUpdateCommand {
        ratings = List.copyOf(ratings);
    }
}
