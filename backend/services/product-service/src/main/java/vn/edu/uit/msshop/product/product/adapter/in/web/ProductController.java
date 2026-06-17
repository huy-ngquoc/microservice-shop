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
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductCommandWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductResponseWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductCreationRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductSimpleCreationRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductInfoUpdateRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductCreationUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductHardDeletionByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductRestorationByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductSoftDeletionByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle.ProductInfoUpdateByIdUseCase;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductCreationUseCase creationUseCase;
    private final ProductRestorationByIdUseCase restorationByIdUseCase;
    private final ProductInfoUpdateByIdUseCase infoUpdateByIdUseCase;
    private final ProductSoftDeletionByIdUseCase softDeletionByIdUseCase;
    private final ProductHardDeletionByIdUseCase hardDeletionByIdUseCase;

    private final ProductCommandWebMapper commandMapper;
    private final ProductResponseWebMapper responseMapper;

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @RequestBody
            @Valid
            final ProductCreationRequest request) {
        final var command = this.commandMapper.toCreationCommand(request);
        final var view = this.creationUseCase.create(command);

        final var response = this.responseMapper.toResponse(view);
        final var method = WebMvcLinkBuilder
                .methodOn(ProductQueryController.class)
                .findActiveById(response.id());
        final var location = WebMvcLinkBuilder
                .linkTo(method)
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/simple")
    public ResponseEntity<ProductResponse> createSimple(
            @RequestBody
            @Valid
            final ProductSimpleCreationRequest request) {
        final var command = this.commandMapper.toSimpleCreationCommand(request);
        final var view = this.creationUseCase.createSimple(command);

        final var response = this.responseMapper.toResponse(view);
        final var method = WebMvcLinkBuilder
                .methodOn(ProductQueryController.class)
                .findActiveById(response.id());
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
        final var command = this.commandMapper.toRestorationByIdCommand(id, version);
        this.restorationByIdUseCase.restore(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateInfoById(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final ProductInfoUpdateRequest request) {
        final var command = this.commandMapper.toInfoUpdateByIdCommand(id, request);
        final var view = this.infoUpdateByIdUseCase.updateInfo(command);

        final var response = this.responseMapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.commandMapper.toSoftDeletionByIdCommand(id, version);
        this.softDeletionByIdUseCase.softDelete(command);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/purge")
    public ResponseEntity<Void> hardDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.commandMapper.toHardDeletionByIdCommand(id, version);
        this.hardDeletionByIdUseCase.hardDelete(command);

        return ResponseEntity.noContent().build();
    }
}
