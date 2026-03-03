package vn.edu.uit.msshop.product.shared.application.dto.response;

import java.util.List;

public record PageResponseDto<T>(
        List<T> items,
        int page,
        int size,
        long totalElements) {
    public PageResponseDto(
            List<T> items,
            int page,
            int size,
            long totalElements) {
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
            this.items = List.of();
        } else {
            this.items = List.copyOf(items);
        }

        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
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
