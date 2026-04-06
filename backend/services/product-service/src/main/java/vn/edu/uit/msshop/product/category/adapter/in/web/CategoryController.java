package vn.edu.uit.msshop.product.category.adapter.in.web;

import java.util.UUID;

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
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryImageRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryInfoRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryImageResponse;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryResponse;
import vn.edu.uit.msshop.product.category.application.port.in.command.CreateCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.command.DeleteCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.command.SoftDeleteCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.command.UpdateCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.command.UpdateCategoryInfoUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.query.CheckCategoryExistsUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.query.FindCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.query.FindCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.query.FindSoftDeletedCategoryUseCase;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final FindCategoryUseCase findUseCase;
    private final FindCategoryImageUseCase findImageUseCase;
    private final CheckCategoryExistsUseCase checkExistsUseCase;
    private final FindSoftDeletedCategoryUseCase findSoftDeletedUseCase;
    private final CreateCategoryUseCase createUseCase;
    private final UpdateCategoryInfoUseCase updateInfoUseCase;
    private final UpdateCategoryImageUseCase updateImageUseCase;
    private final DeleteCategoryImageUseCase deleteImageUseCase;
    private final SoftDeleteCategoryUseCase softDeleteUseCase;
    private final CategoryWebMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findById(this.mapper.toCategoryId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<CategoryImageResponse> findImageById(
            @PathVariable
            final UUID id) {
        final var view = this.findImageUseCase.findImageById(this.mapper.toCategoryId(id));

        final var response = this.mapper.toImageResponse(view);
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
        final var view = this.findSoftDeletedUseCase
                .findSoftDeletedById(this.mapper.toCategoryId(id));

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
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).findById(response.id()))
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

    @PatchMapping("/{id}/image")
    public ResponseEntity<CategoryImageResponse> updateImage(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateCategoryImageRequest request) {
        final var command = this.mapper.toUpdateImageCommand(id, request);
        final var view = this.updateImageUseCase.updateImage(command);

        final var response = this.mapper.toImageResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<CategoryImageResponse> deleteImageById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toDeleteImageCommand(id, version);
        final var view = this.deleteImageUseCase.deleteImage(command);

        final var response = this.mapper.toImageResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toSoftDeleteCommand(id, version);
        this.softDeleteUseCase.delete(command);

        return ResponseEntity.noContent().build();
    }
}
