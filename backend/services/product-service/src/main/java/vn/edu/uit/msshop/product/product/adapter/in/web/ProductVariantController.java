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
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductVariantWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductVariantAdditionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductVariantBulkAdditionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantBulkAdditionUseCase;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductVariantController {
    private final ProductVariantBulkAdditionUseCase bulkAdditionUseCase;

    private final ProductVariantWebMapper variantMapper;
    private final ProductWebMapper mapper;

    @PostMapping("/{id}/variants")
    public ResponseEntity<ProductResponse> addVariant(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final ProductVariantAdditionRequest request) {
        final var command = this.variantMapper.toBulkAdditionCommand(id, request);
        final var view = this.bulkAdditionUseCase.addAll(command);

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/variants/batch")
    public ResponseEntity<ProductResponse> addAllVariants(
            @PathVariable
            final UUID id,
            @RequestBody
            @Valid
            final ProductVariantBulkAdditionRequest request) {
        final var command = this.variantMapper.toBulkAdditionCommand(id, request);
        final var view = this.bulkAdditionUseCase.addAll(command);

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }
}
