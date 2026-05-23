package vn.uit.edu.msshop.rating.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.adapter.out.persistence.PageRequests;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.uit.edu.msshop.rating.adapter.out.persistence.mapper.RatingInfoEntityMapper;
import vn.uit.edu.msshop.rating.application.dto.query.ListRatingInfosQuery;
import vn.uit.edu.msshop.rating.application.port.out.DeleteRatingInfoPort;
import vn.uit.edu.msshop.rating.application.port.out.ListRatingInfosPort;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingInfoPort;
import vn.uit.edu.msshop.rating.application.port.out.SaveRatingInfoPort;
import vn.uit.edu.msshop.rating.domain.model.RatingInfo;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;

@Component
@RequiredArgsConstructor
public class RatingInfoPersistenceAdapter
        implements
        ListRatingInfosPort,
        LoadRatingInfoPort,
        SaveRatingInfoPort,
        DeleteRatingInfoPort {
    private final RatingInfoMongoRepository repository;
    private final RatingInfoEntityMapper mapper;

    @Override
    public PageResponseDto<RatingInfo> list(
            final ListRatingInfosQuery query) {
        final var pageable = PageRequests.toPageable(
                query.pageRequest(),
                RatingInfoDocument.Fields.productId);

        final var page = this.repository.findAllByUpdateAtBetween(
                query.start(),
                query.end(),
                pageable);
        final var ratingInfos = page.getContent().stream()
                .map(this.mapper::toDomain)
                .toList();

        return new PageResponseDto<>(
                ratingInfos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public Optional<RatingInfo> loadById(
            final ProductId productId) {
        return repository.findById(productId.value())
                .map(this.mapper::toDomain);
    }

    @Override
    public List<RatingInfo> loadAll() {
        return repository.findAll().stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public PageResponseDto<RatingInfo> loadByPage(
            final PageRequestDto pageRequest) {
        final var pageable = PageRequests.toPageable(
                pageRequest,
                RatingInfoDocument.Fields.productId);

        final var page = this.repository.findAll(pageable);
        final var ratingInfos = page.getContent().stream()
                .map(this.mapper::toDomain)
                .toList();

        return new PageResponseDto<>(
                ratingInfos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public RatingInfo save(
            final RatingInfo ratingInfo) {
        final var result = repository.save(mapper.toDocument(ratingInfo));
        return mapper.toDomain(result);
    }

    @Override
    public void delete(
            final ProductId productId) {
        repository.deleteById(productId.value());
    }
}
