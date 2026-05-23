package vn.uit.edu.msshop.rating.application.port.in;

import java.util.List;
import java.util.Optional;

import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.uit.edu.msshop.rating.application.dto.view.RatingInfoView;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;

public interface LoadRatingInfoUseCase {
    Optional<RatingInfoView> loadById(
            ProductId id);

    List<RatingInfoView> loadAll();

    PageResponseDto<RatingInfoView> loadAll(
            PageRequestDto pageRequest);
}
