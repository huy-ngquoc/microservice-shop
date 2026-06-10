package vn.edu.uit.msshop.product.product.application.dto.command.rating;

import java.util.List;

import vn.edu.uit.msshop.product.product.application.dto.command.data.ProductRatingData;

public record ProductRatingBulkUpdateCommand(
        List<ProductRatingData> ratingList) {
    public ProductRatingBulkUpdateCommand {
        ratingList = List.copyOf(ratingList);
    }
}
