package vn.uit.edu.msshop.rating.application.port.out;

import org.springframework.data.domain.Page;

import vn.uit.edu.msshop.rating.domain.model.Rating;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;

public interface LoadRatingPort {
    public Rating loadById(RatingId id);
    public Page<Rating> loadByProduct(ProductId id, int pageSize, int pageNumber);
}
