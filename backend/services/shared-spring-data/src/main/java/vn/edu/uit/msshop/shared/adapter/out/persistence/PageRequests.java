package vn.edu.uit.msshop.shared.adapter.out.persistence;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

public final class PageRequests {
    private PageRequests() {
    }

    public static PageRequest toPageable(
            final PageRequestDto request,
            final String defaultSortField) {
        final var direction = switch (request.direction()) {
            case ASC -> Sort.Direction.ASC;
            case DESC -> Sort.Direction.DESC;
        };
        final var sortBy = request.sortBy();
        final var sortField = (sortBy != null) ? sortBy : defaultSortField;
        final var sort = Sort.by(direction, sortField);
        return PageRequest.of(request.page(), request.size(), sort);
    }
}
