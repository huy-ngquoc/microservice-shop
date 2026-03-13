package vn.edu.uit.msshop.product.category.adapter.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.in.web.mapper.CategoryWebMapper;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.CreateCategoryRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryImageRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryInfoRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryImageResponse;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryResponse;
import vn.edu.uit.msshop.product.category.application.port.in.CreateCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.FindCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.UpdateCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.UpdateCategoryInfoUseCase;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final FindCategoryUseCase findUseCase;
    private final CreateCategoryUseCase createUseCase;
    private final UpdateCategoryInfoUseCase updateInfoUseCase;
    private final UpdateCategoryImageUseCase updateImageUseCase;
    private final CategoryWebMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findById(this.mapper.toCategoryId(id));
        final var response = this.mapper.toResponse(view);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody
            @Valid
            final CreateCategoryRequest request) {
        final var command = this.mapper.toCommand(request);
        this.createUseCase.create(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<Void> updateInfo(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateCategoryInfoRequest request) {
        final var command = this.mapper.toCommand(id, request);
        this.updateInfoUseCase.updateInfo(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<CategoryImageResponse> updateImage(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateCategoryImageRequest request) {
        final var command = this.mapper.toCommand(id, request);
        this.updateImageUseCase.updateImage(command);

        return ResponseEntity.noContent().build();
    }
}
