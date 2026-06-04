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
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductSharedWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.application.port.in.query.listing.ListProductsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.listing.ListSoftDeletedProductsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.lookup.FindProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.lookup.FindSoftDeletedProductUseCase;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductQueryController {
    private final ListProductsUseCase listUseCase;
    private final ListSoftDeletedProductsUseCase listSoftDeletedUseCase;
    private final FindProductUseCase findUseCase;
    private final FindSoftDeletedProductUseCase findSoftDeletedUseCase;
    private final ProductSharedWebMapper sharedMapper;

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
        final var views = this.listUseCase.list(request);

        final var response = views.map(this.sharedMapper::toResponse);
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
        final var views = this.listSoftDeletedUseCase.listSoftDeleted(request);

        final var response = views.map(this.sharedMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findById(this.sharedMapper.toProductId(id));

        final var response = this.sharedMapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<ProductResponse> findSoftDeletedById(
            @PathVariable
            final UUID id) {
        final var view = this.findSoftDeletedUseCase.findSoftDeletedById(
                this.sharedMapper.toProductId(id));

        final var response = this.sharedMapper.toResponse(view);
        return ResponseEntity.ok(response);
    }
}
