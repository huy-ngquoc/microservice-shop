package vn.uit.edu.msshop.rating.application.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.dto.query.RatingView;
import vn.uit.edu.msshop.rating.application.mapper.RatingViewMapper;
import vn.uit.edu.msshop.rating.application.port.in.FindRatingUseCase;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingPort;
import vn.uit.edu.msshop.rating.domain.model.Rating;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;

@Service
@RequiredArgsConstructor
public class FindRatingService implements FindRatingUseCase {
    private final LoadRatingPort loadPort;
    private final RatingViewMapper mapper;
    @Override
    public RatingView findById(RatingId ratingId) {
        final var rating = loadPort.loadById(ratingId);
        return mapper.toView(rating);
    }

    @Override
    public Page<RatingView> findByProductId(ProductId productId, int pageSize, int PageNumber) {
        Page<Rating> result = loadPort.loadByProduct(productId, pageSize, PageNumber);
        return result.map(mapper::toView);
    }

    

}
