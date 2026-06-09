package vn.edu.uit.msshop.product.product.adapter.in.web;

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
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateProductRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateSimpleProductRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductInfoRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductCreationUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductHardDeletionUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductRestorationUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductSoftDeletionUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductInfoUpdateUseCase;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductCreationUseCase creationUseCase;
    private final ProductRestorationUseCase restorationUseCase;
    private final ProductInfoUpdateUseCase infoUpdateUseCase;
    private final ProductSoftDeletionUseCase softDeletionUseCase;
    private final ProductHardDeletionUseCase hardDeletionUseCase;

    private final ProductWebMapper mapper;

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @RequestBody
            @Valid
            final CreateProductRequest request) {
        final var view = this.creationUseCase.create(this.mapper.toCreationCommand(request));

        final var response = this.mapper.toResponse(view);
        final var method = WebMvcLinkBuilder
                .methodOn(ProductQueryController.class)
                .findById(response.id());
        final var location = WebMvcLinkBuilder
                .linkTo(method)
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/simple")
    public ResponseEntity<ProductResponse> createSimple(
            @RequestBody
            @Valid
            final CreateSimpleProductRequest request) {
        final var view = this.creationUseCase.createSimple(this.mapper.toSimpleCreationCommand(request));

        final var response = this.mapper.toResponse(view);
        final var method = WebMvcLinkBuilder
                .methodOn(ProductQueryController.class)
                .findById(response.id());
        final var location = WebMvcLinkBuilder
                .linkTo(method)
                .toUri();

        return ResponseEntity.created(location).body(response);
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

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateInfo(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateProductInfoRequest request) {
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
