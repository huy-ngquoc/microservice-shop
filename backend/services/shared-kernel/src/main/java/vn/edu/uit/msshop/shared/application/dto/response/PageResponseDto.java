package vn.edu.uit.msshop.shared.application.dto.response;

import java.util.List;
import java.util.function.Function;

public record PageResponseDto<T>(
        List<T> items,
        int page,
        int size,
        long totalElements) {
    public PageResponseDto {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be >= 0");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Size must be > 0");
        }

        if (totalElements < 0) {
            throw new IllegalArgumentException("totalElements must be >= 0");
        }

        if (items == null) {
            items = List.of();
        } else {
            items = List.copyOf(items);
        }
    }

    public <R> PageResponseDto<R> map(
            final Function<T, R> mapper) {
        final var mapped = items.stream().map(mapper).toList();
        return new PageResponseDto<>(mapped, page, size, totalElements);
    }

    public int totalPages() {
        if (totalElements == 0) {
            return 0;
        }

        return (int) Math.ceil((double) totalElements / (double) size);
    }

    public boolean hasNext() {
        return (page + 1) < this.totalPages();
    }

    public boolean hasPrev() {
        return (page > 0) && (this.totalPages() > 0);
    }
}
