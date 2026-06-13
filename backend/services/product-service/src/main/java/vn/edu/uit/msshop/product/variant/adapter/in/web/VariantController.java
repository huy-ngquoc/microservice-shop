package vn.edu.uit.msshop.product.variant.adapter.in.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;
import vn.edu.uit.msshop.product.variant.adapter.in.web.mapper.VariantWebMapper;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantInfoRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantResponse;
import vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle.VariantHardDeletionByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle.VariantRestorationByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle.VariantSoftDeletionByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle.VariantInfoUpdateByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.query.listing.VariantActiveListingUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.query.lookup.VariantActiveLookupByIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.query.lookup.VariantSoftDeletedLookupByIdUseCase;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
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
    private final VariantActiveListingUseCase activeListingUseCase;
    private final VariantActiveLookupByIdUseCase activeLookupByIdUseCase;
    private final VariantSoftDeletedLookupByIdUseCase softDeletedLookupByIdUseCase;
    private final VariantInfoUpdateByIdUseCase updateInfoByIdUseCase;
    private final VariantSoftDeletionByIdUseCase softDeletionByIdUseCase;
    private final VariantRestorationByIdUseCase restorationByIdUseCase;
    private final VariantHardDeletionByIdUseCase hardDeletionByIdUseCase;

    private final VariantWebMapper mapper;

    @GetMapping
    public ResponseEntity<PageResponseDto<VariantResponse>> list(
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
            final PageRequestDto.Direction direction,

            @RequestParam(
                    name = "target",
                    required = false)
            @Nullable
            final List<String> targets) {
        final var query = this.mapper.toListQuery(
                page,
                size,
                sortBy,
                direction,
                targets);
        final var views = this.activeListingUseCase.list(query);

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariantResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.activeLookupByIdUseCase.findById(this.mapper.toVariantId(id));
        final var response = this.mapper.toResponse(view);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<VariantResponse> findSoftDeletedById(
            @PathVariable
            final UUID id) {
        final var view = this.softDeletedLookupByIdUseCase
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
        this.restorationByIdUseCase.restore(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<VariantResponse> updateInfoById(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateVariantInfoRequest request) {
        final var command = this.mapper.toUpdateInfoCommand(id, request);
        final var view = this.updateInfoByIdUseCase.updateInfo(command);

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
        this.softDeletionByIdUseCase.delete(command);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/purge")
    public ResponseEntity<Void> hardDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toHardDeleteCommand(id, version);
        this.hardDeletionByIdUseCase.purge(command);

        return ResponseEntity.noContent().build();
    }
}
