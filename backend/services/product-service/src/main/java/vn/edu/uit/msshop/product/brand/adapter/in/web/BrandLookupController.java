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
import vn.edu.uit.msshop.product.brand.adapter.in.web.mapper.BrandSharedWebMapper;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.brand.application.port.in.query.BrandLookupUseCases;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandLookupController {

    private final BrandLookupUseCases.ListActive listUseCase;
    private final BrandLookupUseCases.ListSoftDeleted listSoftDeletedUseCase;
    private final BrandLookupUseCases.FindActiveById findActiveByIdUseCase;
    private final BrandLookupUseCases.FindSoftDeletedById findSoftDeletedByIdUseCase;
    private final BrandLookupUseCases.CheckExistsById checkExistsByIdUseCase;

    private final BrandSharedWebMapper sharedMapper;

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
        final var views = listUseCase.listActive(request);

        final var response = views.map(this.sharedMapper::toResponse);
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
        final var views = listSoftDeletedUseCase.listSoftDeleted(request);

        final var response = views.map(this.sharedMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findActiveByIdUseCase.findActiveById(
                this.sharedMapper.toBrandId(id));

        final var response = this.sharedMapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<BrandResponse> findSoftDeletedById(
            @PathVariable
            final UUID id) {
        final var view = this.findSoftDeletedByIdUseCase.findSoftDeletedById(
                this.sharedMapper.toBrandId(id));

        final var response = this.sharedMapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> existsById(
            @PathVariable
            final UUID id) {
        final var existed = this.checkExistsByIdUseCase.existsById(
                this.sharedMapper.toBrandId(id));
        if (!existed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
