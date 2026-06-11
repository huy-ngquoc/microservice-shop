package vn.edu.uit.msshop.product.brand.adapter.in.web;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.in.web.mapper.BrandWebMapper;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.brand.application.port.in.query.existence.BrandActiveExistenceCheckByIdUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.query.listing.BrandActiveListingUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.query.listing.BrandSoftDeletedListingUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.query.lookup.BrandActiveLookupByIdUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.query.lookup.BrandSoftDeletedLookupByIdUseCase;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandLookupController {

    private final BrandActiveListingUseCase activeListingUseCase;
    private final BrandSoftDeletedListingUseCase softDeletedListingUseCase;
    private final BrandActiveExistenceCheckByIdUseCase activeExistenceCheckByIdUseCase;
    private final BrandActiveLookupByIdUseCase activeLookupByIdUseCase;
    private final BrandSoftDeletedLookupByIdUseCase softDeletedLookupByIdUseCase;

    private final BrandWebMapper mapper;

    @GetMapping
    public ResponseEntity<PageResponseDto<BrandResponse>> list(
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
        final var query = this.mapper.toActiveListingQuery(request);
        final var views = activeListingUseCase.list(query);

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted")
    public ResponseEntity<PageResponseDto<BrandResponse>> listSoftDeleted(
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
        final var query = this.mapper.toSoftDeletedListingQuery(request);
        final var views = softDeletedListingUseCase.list(query);

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> findById(
            @PathVariable
            final UUID id) {
        final var query = this.mapper.toActiveLookupByIdQuery(id);
        final var view = this.activeLookupByIdUseCase.find(query);

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<BrandResponse> findSoftDeletedById(
            @PathVariable
            final UUID id) {
        final var query = this.mapper.toSoftDeletedLookupByIdQuery(id);
        final var view = this.softDeletedLookupByIdUseCase.find(query);

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> existsById(
            @PathVariable
            final UUID id) {
        final var query = this.mapper.toActiveExistenceCheckByIdQuery(id);
        final var existed = this.activeExistenceCheckByIdUseCase.exists(query);

        if (!existed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
