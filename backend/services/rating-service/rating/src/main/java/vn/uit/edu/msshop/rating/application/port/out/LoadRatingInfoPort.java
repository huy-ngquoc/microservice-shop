package vn.uit.edu.msshop.rating.application.port.out;

import java.util.List;
import java.util.Optional;

import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.uit.edu.msshop.rating.domain.model.RatingInfo;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;

public interface LoadRatingInfoPort {
    Optional<RatingInfo> loadById(
            ProductId productId);

    List<RatingInfo> loadAll();

    PageResponseDto<RatingInfo> loadByPage(
            PageRequestDto pageRequest);
}
