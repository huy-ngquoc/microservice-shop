package vn.edu.uit.msshop.product.product.adapter.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductResponseWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductVariantWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductVariantRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductVariantsRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.application.port.in.command.AddProductVariantsUseCase;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductVariantController {
    private final AddProductVariantsUseCase addVariantsUseCase;
    private final ProductVariantWebMapper mapper;
    private final ProductResponseWebMapper responseMapper;

    @PostMapping("/{id}/variants")
    public ResponseEntity<ProductResponse> addVariant(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final AddProductVariantRequest request) {
        final var command = this.mapper.toAddVariantsCommand(id, request);
        final var view = this.addVariantsUseCase.addVariants(command);
        return ResponseEntity.ok(this.responseMapper.toResponse(view));
    }

    @PostMapping("/{id}/variants/batch")
    public ResponseEntity<ProductResponse> addAllVariants(
            @PathVariable
            final UUID id,
            @RequestBody
            @Valid
            final AddProductVariantsRequest request) {
        final var command = this.mapper.toAddVariantsCommand(id, request);
        final var view = this.addVariantsUseCase.addVariants(command);
        return ResponseEntity.ok(this.responseMapper.toResponse(view));
    }
}
