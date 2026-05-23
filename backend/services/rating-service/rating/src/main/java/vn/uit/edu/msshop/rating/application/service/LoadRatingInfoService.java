package vn.uit.edu.msshop.rating.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.uit.edu.msshop.rating.application.dto.view.RatingInfoView;
import vn.uit.edu.msshop.rating.application.mapper.RatingInfoViewMapper;
import vn.uit.edu.msshop.rating.application.port.in.LoadRatingInfoUseCase;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingInfoPort;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class LoadRatingInfoService
        implements LoadRatingInfoUseCase {
    private final LoadRatingInfoPort loadPort;
    private final RatingInfoViewMapper mapper;

    @Override
    public Optional<RatingInfoView> loadById(
            ProductId id) {
        return loadPort.loadById(id)
                .map(this.mapper::toView);
    }

    @Override
    public List<RatingInfoView> loadAll() {
        final var result = loadPort.loadAll();
        return result.stream()
                .map(mapper::toView)
                .toList();
    }

    @Override
    public PageResponseDto<RatingInfoView> loadAll(
            PageRequestDto pageable) {
        final var result = loadPort.loadByPage(pageable);
        return result.map(mapper::toView);
    }
}
