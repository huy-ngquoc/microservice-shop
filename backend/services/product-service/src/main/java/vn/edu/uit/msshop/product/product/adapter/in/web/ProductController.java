package vn.edu.uit.msshop.product.product.adapter.in.web;

import java.util.UUID;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateProductRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductInfoRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.application.port.in.command.CreateProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.UpdateProductInfoUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckProductExistsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.FindProductUseCase;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final FindProductUseCase findUseCase;
    private final CheckProductExistsUseCase checkExistsUseCase;
    private final CreateProductUseCase createUseCase;
    private final UpdateProductInfoUseCase updateInfoUseCase;
    private final ProductWebMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findById(this.mapper.toProductId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> existsById(
            @PathVariable
            final UUID id) {
        final var existed = this.checkExistsUseCase.existsById(this.mapper.toProductId(id));
        if (!existed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @RequestBody
            @Valid
            final CreateProductRequest request) {
        final ProductView view;
        if (request.variants().isEmpty() && request.options().isEmpty()) {
            view = this.createUseCase.createSimple(this.mapper.toCreateSimpleCommand(request));
        } else {
            view = this.createUseCase.create(this.mapper.toCreateCommand(request));
        }

        final var response = this.mapper.toResponse(view);
        final var location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).findById(response.id()))
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateInfo(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateProductInfoRequest request) {
        final var command = this.mapper.toUpdateInfoCommand(id, request);
        final var view = this.updateInfoUseCase.updateInfo(command);

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }
}
