package vn.edu.uit.msshop.product.shared.application.dto.request;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public record PageRequestDto(
        int page,
        int size,
        @Nullable
        String sortBy,
        Direction direction) {
    public enum Direction {
        ASC,
        DESC
    }

    public PageRequestDto(
            final int page,
            final int size,
            @Nullable
            final String sortBy,
            final Direction direction) {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be >= 0");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Size must be > 0");
        }

        this.page = page;
        this.size = size;

        if ((sortBy == null) || (sortBy.isBlank())) {
            this.sortBy = null;
        } else {
            this.sortBy = sortBy.trim();
        }

        this.direction = Objects.requireNonNullElse(direction, Direction.ASC);
    }

    public PageRequestDto(
            final int page,
            final int size) {
        this(page, size, null, Direction.ASC);
    }

    public int offset() {
        return Math.multiplyExact(page, size);
    }
}
