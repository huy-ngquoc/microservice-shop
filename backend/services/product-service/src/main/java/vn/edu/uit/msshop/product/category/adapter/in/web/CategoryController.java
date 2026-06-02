package vn.edu.uit.msshop.product.category.adapter.in.web;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.in.web.mapper.CategoryWebMapper;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.CreateCategoryRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryInfoRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryResponse;
import vn.edu.uit.msshop.product.category.application.port.in.command.CategoryLifecycleUseCases;
import vn.edu.uit.msshop.product.category.application.port.in.query.CategoryLookupUseCases;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryLookupUseCases.ListActive listUseCase;
    private final CategoryLookupUseCases.ListSoftDeleted listSoftDeletedUseCase;
    private final CategoryLookupUseCases.FindActiveById findUseCase;
    private final CategoryLookupUseCases.CheckExistsById checkExistsUseCase;
    private final CategoryLookupUseCases.FindSoftDeletedById findSoftDeletedUseCase;
    private final CategoryLifecycleUseCases.Create createUseCase;
    private final CategoryLifecycleUseCases.Restore restoreUseCase;
    private final CategoryLifecycleUseCases.UpdateInfo updateInfoUseCase;
    private final CategoryLifecycleUseCases.SoftDelete softDeleteUseCase;
    private final CategoryLifecycleUseCases.HardDelete hardDeleteUseCase;

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

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findActiveById(this.mapper.toCategoryId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> existsById(
            @PathVariable
            final UUID id) {
        final var existed = this.checkExistsUseCase.existsById(this.mapper.toCategoryId(id));
        if (!existed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<CategoryResponse> findSoftDeletedById(
            @PathVariable
            final UUID id) {
        final var view = this.findSoftDeletedUseCase.findSoftDeletedById(this.mapper.toCategoryId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @RequestBody
            @Valid
            final CreateCategoryRequest request) {
        final var command = this.mapper.toCreateCommand(request);
        final var view = this.createUseCase.create(command);

        final var response = this.mapper.toResponse(view);
        final var location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).findById(response.id())).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toRestoreCommand(id, version);
        this.restoreUseCase.restore(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<CategoryResponse> updateInfo(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateCategoryInfoRequest request) {
        final var command = this.mapper.toUpdateInfoCommand(id, request);
        final var view = this.updateInfoUseCase.updateInfo(command);

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toSoftDeleteCommand(id, version);
        this.softDeleteUseCase.softDelete(command);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/purge")
    public ResponseEntity<Void> hardDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toHardDeleteCommand(id, version);
        this.hardDeleteUseCase.hardDelete(command);

        return ResponseEntity.noContent().build();
    }
}
