package vn.uit.edu.msshop.rating.application.port.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.uit.edu.msshop.rating.domain.model.RatingInfo;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;


public interface LoadRatingInfoPort {
    public Optional<RatingInfo> loadById(ProductId productId);
    public List<RatingInfo> loadAll();
    public Page<RatingInfo> loadByPage(Pageable pageable);
}
