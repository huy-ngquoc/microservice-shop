package vn.uit.edu.msshop.rating.application.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.dto.query.RatingInfoView;
import vn.uit.edu.msshop.rating.application.mapper.RatingInfoViewMapper;
import vn.uit.edu.msshop.rating.application.port.in.LoadRatingInfoUseCase;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingInfoPort;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class LoadRatingInfoService implements LoadRatingInfoUseCase {
    private final LoadRatingInfoPort loadPort;
    private final RatingInfoViewMapper mapper;

    @Override
    public Optional<RatingInfoView> loadById(ProductId id) {
        final var result = loadPort.loadById(id);
        if(result.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toView(result.get()));
    }

    @Override
    public List<RatingInfoView> loadAll() {
        final var result = loadPort.loadAll();
        return result.stream().map(mapper::toView).toList();
    }

    @Override
    public Page<RatingInfoView> loadAll(Pageable pageable) {
        final var result = loadPort.loadByPage(pageable);
        return result.map(mapper::toView);
    }

    @Override
    public Page<RatingInfoView> loadUpdatedRatingInfo(Instant start, Instant end, Pageable pageable) {
        final var result = loadPort.loadUpdatedRatingInfo(start, end, pageable);
        return result.map(mapper::toView);
    }
}
