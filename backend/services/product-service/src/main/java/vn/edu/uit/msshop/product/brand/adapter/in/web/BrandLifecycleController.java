package vn.edu.uit.msshop.product.brand.adapter.in.web;

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
import vn.edu.uit.msshop.product.brand.adapter.in.web.mapper.BrandWebMapper;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.CreateBrandRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandInfoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle.BrandCreationUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle.BrandHardDeletionUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle.BrandInfoUpdateUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle.BrandRestorationUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.command.lifecycle.BrandSoftDeletionUseCase;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandLifecycleController {

    private final BrandCreationUseCase creationUseCase;
    private final BrandRestorationUseCase restorationUseCase;
    private final BrandInfoUpdateUseCase infoUpdateUseCase;
    private final BrandSoftDeletionUseCase softDeletionUseCase;
    private final BrandHardDeletionUseCase hardDeletionUseCase;

    private final BrandWebMapper mapper;

    @PostMapping
    public ResponseEntity<BrandResponse> create(
            @RequestBody
            @Valid
            final CreateBrandRequest request) {
        final var command = this.mapper.toCreationCommand(request);
        final var view = this.creationUseCase.create(command);

        final var response = this.mapper.toResponse(view);
        final var method = WebMvcLinkBuilder
                .methodOn(BrandLookupController.class)
                .findById(response.id());
        final var location = WebMvcLinkBuilder
                .linkTo(method)
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<BrandResponse> updateInfo(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateBrandInfoRequest request) {
        final var command = this.mapper.toInfoUpdateCommand(id, request);
        final var view = this.infoUpdateUseCase.updateInfo(command);

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toSoftDeletionCommand(id, version);
        this.softDeletionUseCase.softDelete(command);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restoreById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toRestorationCommand(id, version);
        this.restorationUseCase.restore(command);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/purge")
    public ResponseEntity<Void> hardDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toHardDeletionCommand(id, version);
        this.hardDeletionUseCase.hardDelete(command);

        return ResponseEntity.noContent().build();
    }
}
