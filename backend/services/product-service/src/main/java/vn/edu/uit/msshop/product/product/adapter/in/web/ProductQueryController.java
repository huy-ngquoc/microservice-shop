package vn.edu.uit.msshop.product.product.adapter.in.web;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.application.port.in.query.listing.ProductActiveListingUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.listing.ProductSoftDeletedListingUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.lookup.ProductActiveLookupByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.lookup.ProductSoftDeletedLookupByIdUseCase;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductQueryController {
    private final ProductActiveListingUseCase activeListingUseCase;
    private final ProductSoftDeletedListingUseCase softDeletedListingUseCase;
    private final ProductActiveLookupByIdUseCase activeLookupByIdUseCase;
    private final ProductSoftDeletedLookupByIdUseCase softDeletedLookupByIdUseCase;

    private final ProductWebMapper mapper;

    @GetMapping
    public ResponseEntity<PageResponseDto<ProductResponse>> list(
            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_PAGE_STRING)
            final int page,

            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_SIZE_STRING)
            final int size,

            @RequestParam(
                    required = false)
            @Nullable
            final String sortBy,

            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_DIRECTION_STRING)
            final PageRequestDto.Direction direction) {
        final var request = new PageRequestDto(page, size, sortBy, direction);
        final var views = this.activeListingUseCase.list(request);

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted")
    public ResponseEntity<PageResponseDto<ProductResponse>> listSoftDeleted(
            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_PAGE_STRING)
            final int page,

            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_SIZE_STRING)
            final int size,

            @RequestParam(
                    required = false)
            @Nullable
            final String sortBy,

            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_DIRECTION_STRING)
            final PageRequestDto.Direction direction) {
        final var request = new PageRequestDto(page, size, sortBy, direction);
        final var views = this.softDeletedListingUseCase.list(request);

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.activeLookupByIdUseCase.findById(this.mapper.toProductId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<ProductResponse> findSoftDeletedById(
            @PathVariable
            final UUID id) {
        final var view = this.softDeletedLookupByIdUseCase.findById(
                this.mapper.toProductId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }
}
