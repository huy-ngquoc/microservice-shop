package vn.edu.uit.msshop.product.category.adapter.in.web;

import java.util.UUID;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryLifecycleController {

    private final CategoryLifecycleUseCases.Create createUseCase;
    private final CategoryLifecycleUseCases.UpdateInfo updateInfoUseCase;
    private final CategoryLifecycleUseCases.SoftDelete softDeleteUseCase;
    private final CategoryLifecycleUseCases.Restore restoreUseCase;
    private final CategoryLifecycleUseCases.HardDelete hardDeleteUseCase;

    private final CategoryWebMapper mapper;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @RequestBody
            @Valid
            final CreateCategoryRequest request) {
        final var command = this.mapper.toCreateCommand(request);
        final var view = this.createUseCase.create(command);

        final var response = this.mapper.toResponse(view);
        final var method = WebMvcLinkBuilder
                .methodOn(CategoryLookupController.class)
                .findActiveById(response.id());
        final var location = WebMvcLinkBuilder
                .linkTo(method)
                .toUri();

        return ResponseEntity.created(location).body(response);
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
