package vn.uit.edu.msshop.rating.application.port.in;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.uit.edu.msshop.rating.application.dto.query.RatingInfoView;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;

public interface LoadRatingInfoUseCase {
    public Optional<RatingInfoView> loadById(ProductId id);
    public List<RatingInfoView> loadAll();
    public Page<RatingInfoView> loadAll(Pageable pageable);
}
