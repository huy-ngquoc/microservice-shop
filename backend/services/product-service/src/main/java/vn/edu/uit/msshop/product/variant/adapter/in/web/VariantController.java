package vn.edu.uit.msshop.product.variant.adapter.in.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.in.web.mapper.VariantWebMapper;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantImageRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantInfoRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantImageResponse;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantResponse;
import vn.edu.uit.msshop.product.variant.application.port.in.command.DeleteVariantImageUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.RestoreVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SoftDeleteVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.UpdateVariantImageUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.UpdateVariantInfoUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.query.FindSoftDeletedVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.query.FindVariantImageUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.query.FindVariantUseCase;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/variants")
@RequiredArgsConstructor
public class VariantController {
    private final FindVariantUseCase findUseCase;
    private final FindVariantImageUseCase findImageUseCase;
    private final FindSoftDeletedVariantUseCase findSoftDeletedUseCase;
    private final RestoreVariantUseCase restoreUseCase;
    private final UpdateVariantInfoUseCase updateInfoUseCase;
    private final UpdateVariantImageUseCase updateImageUseCase;
    private final DeleteVariantImageUseCase deleteImageUseCase;
    private final SoftDeleteVariantUseCase softDeleteUseCase;
    private final VariantWebMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<VariantResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findById(this.mapper.toVariantId(id));
        final var response = this.mapper.toResponse(view);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<VariantImageResponse> findImageById(
            @PathVariable
            final UUID id) {
        final var view = this.findImageUseCase.findImageById(this.mapper.toVariantId(id));

        final var response = this.mapper.toImageResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<VariantResponse> findSoftDeletedById(
            @PathVariable
            final UUID id) {
        final var view = this.findSoftDeletedUseCase
                .findSoftDeletedById(this.mapper.toVariantId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restoreById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toRestoreCommand(id, version);
        this.restoreUseCase.restore(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<VariantResponse> updateInfo(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateVariantInfoRequest request) {
        final var command = this.mapper.toUpdateInfoCommand(id, request);
        final var view = this.updateInfoUseCase.updateInfo(command);

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<VariantImageResponse> updateImage(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateVariantImageRequest request) {
        final var command = this.mapper.toUpdateImageCommand(id, request);
        final var view = this.updateImageUseCase.updateImage(command);

        final var response = this.mapper.toImageResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<VariantImageResponse> deleteImageById(
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
        final var command = this.mapper.toSoftDeleteVariantCommand(id, version);
        this.softDeleteUseCase.delete(command);

        return ResponseEntity.noContent().build();
    }
}
