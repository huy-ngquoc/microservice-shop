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
import vn.edu.uit.msshop.product.brand.application.port.in.command.BrandLifecycleUseCases;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandLifecycleController {

    private final BrandLifecycleUseCases.Create createUseCase;
    private final BrandLifecycleUseCases.Restore restoreUseCase;
    private final BrandLifecycleUseCases.UpdateInfo updateInfoUseCase;
    private final BrandLifecycleUseCases.SoftDelete softDeleteUseCase;
    private final BrandLifecycleUseCases.HardDelete hardDeleteUseCase;

    private final BrandWebMapper mapper;

    @PostMapping
    public ResponseEntity<BrandResponse> create(
            @RequestBody
            @Valid
            final CreateBrandRequest request) {
        final var command = this.mapper.toCreationCommand(request);
        final var view = this.createUseCase.create(command);

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
        final var command = this.mapper.toSoftDeletionCommand(id, version);
        this.softDeleteUseCase.softDelete(command);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restoreById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toRestorationCommand(id, version);
        this.restoreUseCase.restore(command);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/purge")
    public ResponseEntity<Void> hardDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toHardDeletionCommand(id, version);
        this.hardDeleteUseCase.hardDelete(command);

        return ResponseEntity.noContent().build();
    }
}
