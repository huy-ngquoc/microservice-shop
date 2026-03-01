package vn.uit.edu.msshop.rating.application.port.in;

import org.springframework.data.domain.Page;

import vn.uit.edu.msshop.rating.application.dto.query.RatingView;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;

public interface FindRatingUseCase {
    public RatingView findById(RatingId ratingId);
    public Page<RatingView> findByProductId(ProductId productId, int pageSize, int PageNumber);
    
}
