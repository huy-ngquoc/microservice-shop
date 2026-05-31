package vn.uit.edu.msshop.rating.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.uit.edu.msshop.rating.application.dto.query.ListRatingInfosQuery;
import vn.uit.edu.msshop.rating.application.dto.view.RatingInfoView;
import vn.uit.edu.msshop.rating.application.mapper.RatingInfoViewMapper;
import vn.uit.edu.msshop.rating.application.port.in.ListRatingInfosUseCase;
import vn.uit.edu.msshop.rating.application.port.out.ListRatingInfosPort;

@Service
@RequiredArgsConstructor
public class ListRatingInfosService
        implements ListRatingInfosUseCase {
    private final ListRatingInfosPort listPort;
    private final RatingInfoViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<RatingInfoView> list(
            ListRatingInfosQuery query) {
        final var page = this.listPort.list(query);
        return page.map(this.mapper::toView);
    }
}
