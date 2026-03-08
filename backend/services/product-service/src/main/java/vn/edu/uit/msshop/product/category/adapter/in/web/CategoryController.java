package vn.edu.uit.msshop.product.category.adapter.in.web;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.in.web.mapper.CategoryWebMapper;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.CreateCategoryRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.request.UpdateCategoryInfoRequest;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryImageResponse;
import vn.edu.uit.msshop.product.category.adapter.in.web.response.CategoryResponse;
import vn.edu.uit.msshop.product.category.application.port.in.CreateCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.FindCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.FindCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.UpdateCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.UpdateCategoryInfoUseCase;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final FindCategoryUseCase findUseCase;
    private final FindCategoryImageUseCase findImageUseCase;
    private final CreateCategoryUseCase createUseCase;
    private final UpdateCategoryInfoUseCase updateInfoUseCase;
    private final UpdateCategoryImageUseCase updateImageUseCase;
    private final CategoryWebMapper webMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findById(this.webMapper.toCategoryId(id));
        final var response = this.webMapper.toResponse(view);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<CategoryImageResponse> findImageById(
            @PathVariable
            final UUID id) {
        final var view = this.findImageUseCase.findById(this.webMapper.toCategoryId(id));
        final var response = this.webMapper.toResponse(view);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody
            final CreateCategoryRequest request) {
        final var command = this.webMapper.toCommand(request);
        this.createUseCase.create(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<Void> updateInfo(
            @PathVariable
            final UUID id,

            @RequestBody
            final UpdateCategoryInfoRequest request) {
        final var command = this.webMapper.toCommand(id, request);
        this.updateInfoUseCase.updateInfo(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(
            value = "/{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryImageResponse> updateImage(
            @PathVariable
            final UUID id,

            @RequestPart("file")
            final MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        final var contentType = file.getContentType();
        if ((contentType == null || !contentType.startsWith("image/"))) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "image";
        }

        final var command = this.webMapper.toCommand(
                id,
                file.getBytes(),
                originalFilename,
                contentType);

        final var view = this.updateImageUseCase.updateImage(command);
        final var response = this.webMapper.toResponse(view);

        return ResponseEntity.ok(response);
    }
}
