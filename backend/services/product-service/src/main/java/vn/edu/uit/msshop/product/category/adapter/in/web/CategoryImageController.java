package vn.edu.uit.msshop.product.category.adapter.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.in.web.mapper.CategoryImageWebMapper;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryImageRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryImageResponse;
import vn.edu.uit.msshop.product.category.application.port.in.command.image.CategoryImageDeletionByIdUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.command.image.CategoryImageUpdateByIdUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.query.lookup.CategoryImageActiveLookupByIdUseCase;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryImageController {

    private final CategoryImageActiveLookupByIdUseCase activeLookupByIdUseCase;
    private final CategoryImageUpdateByIdUseCase updateByIdUseCase;
    private final CategoryImageDeletionByIdUseCase deletionByIdUseCase;

    private final CategoryImageWebMapper mapper;

    @GetMapping("/{id}/image")
    public ResponseEntity<CategoryImageResponse> findById(
            @PathVariable
            final UUID id) {
        final var query = this.mapper.toActiveLookupByIdQuery(id);
        final var view = this.activeLookupByIdUseCase.find(query);

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<CategoryImageResponse> update(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateCategoryImageRequest request) {
        final var command = this.mapper.toImageUpdateByIdCommand(id, request);
        final var view = this.updateByIdUseCase.update(command);

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Void> deleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toImageDeletionByIdCommand(id, version);
        this.deletionByIdUseCase.delete(command);

        return ResponseEntity.noContent().build();
    }
}
