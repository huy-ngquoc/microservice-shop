package vn.edu.uit.msshop.product.category.adapter.in.web;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.in.web.mapper.CategoryWebMapper;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryResponse;
import vn.edu.uit.msshop.product.category.application.port.in.query.CategoryLookupUseCases;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryLookupController {

    private final CategoryLookupUseCases.ListActive listUseCase;
    private final CategoryLookupUseCases.ListSoftDeleted listSoftDeletedUseCase;
    private final CategoryLookupUseCases.CheckExistsById checkExistsByIdUseCase;
    private final CategoryLookupUseCases.FindActiveById findActiveByIdUseCase;
    private final CategoryLookupUseCases.FindSoftDeletedById findSoftDeletedByIdUseCase;

    private final CategoryWebMapper mapper;

    @GetMapping
    public ResponseEntity<PageResponseDto<CategoryResponse>> list(
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

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted")
    public ResponseEntity<PageResponseDto<CategoryResponse>> listSoftDeleted(
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

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> existsById(
            @PathVariable
            final UUID id) {
        final var existed = this.checkExistsByIdUseCase.existsById(
                this.mapper.toCategoryId(id));
        if (!existed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findActiveById(
            @PathVariable
            final UUID id) {
        final var view = this.findActiveByIdUseCase.findActiveById(
                this.mapper.toCategoryId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<CategoryResponse> findSoftDeletedById(
            @PathVariable
            final UUID id) {
        final var view = this.findSoftDeletedByIdUseCase.findSoftDeletedById(
                this.mapper.toCategoryId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

}
