package vn.edu.uit.msshop.product.variant.adapter.in.web;

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
import vn.edu.uit.msshop.product.variant.adapter.in.web.mapper.VariantImageWebMapper;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantImageRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantImageResponse;
import vn.edu.uit.msshop.product.variant.application.port.in.command.image.VariantImageDeletionByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.image.VariantImageUpdateByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.query.lookup.VariantActiveImageLookupByIdUseCase;

@RestController
@RequestMapping("/variants")
@RequiredArgsConstructor
public class VariantImageController {
    private final VariantActiveImageLookupByIdUseCase activeLookupByIdUseCase;
    private final VariantImageUpdateByIdUseCase updateByIdUseCase;
    private final VariantImageDeletionByIdUseCase deletionByIdUseCase;

    private final VariantImageWebMapper mapper;

    @GetMapping("/{id}/image")
    public ResponseEntity<VariantImageResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.activeLookupByIdUseCase
                .findImageById(this.mapper.toVariantId(id));

        final var response = this.mapper.toImageResponse(view);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<VariantImageResponse> updateById(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateVariantImageRequest request) {
        final var command = this.mapper.toUpdateImageCommand(id, request);
        final var view = this.updateByIdUseCase.updateImage(command);

        final var response = this.mapper.toImageResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<VariantImageResponse> deleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toDeleteImageCommand(id, version);
        final var view = this.deletionByIdUseCase.deleteImage(command);

        final var response = this.mapper.toImageResponse(view);
        return ResponseEntity.ok(response);
    }
}
