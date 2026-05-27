package vn.edu.uit.msshop.product.product.adapter.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductOptionWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductSharedWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.RemoveProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.application.port.in.command.AddProductOptionUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.RemoveProductOptionUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.UpdateProductOptionUseCase;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductOptionController {
    private final AddProductOptionUseCase addOptionUseCase;
    private final UpdateProductOptionUseCase updateOptionUseCase;
    private final RemoveProductOptionUseCase removeOptionUseCase;
    private final ProductOptionWebMapper mapper;
    private final ProductSharedWebMapper sharedMapper;

    @PostMapping("/{id}/options")
    public ResponseEntity<ProductResponse> addOption(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final AddProductOptionRequest request) {
        final var command = this.mapper.toAddOptionCommand(id, request);
        final var view = this.addOptionUseCase.addOption(command);
        return ResponseEntity.ok(this.sharedMapper.toResponse(view));
    }

    @PatchMapping("/{id}/options/{index}")
    public ResponseEntity<ProductResponse> updateOption(
            @PathVariable
            final UUID id,

            @PathVariable
            final int index,

            @RequestBody
            @Valid
            final UpdateProductOptionRequest request) {
        final var command = this.mapper.toUpdateOptionCommand(id, index, request);
        final var view = this.updateOptionUseCase.updateOption(command);
        return ResponseEntity.ok(this.sharedMapper.toResponse(view));
    }

    @DeleteMapping("/{id}/options/{index}")
    public ResponseEntity<ProductResponse> removeOption(
            @PathVariable
            final UUID id,

            @PathVariable
            final int index,

            @RequestBody
            @Valid
            final RemoveProductOptionRequest request) {
        final var command = this.mapper.toRemoveOptionCommand(id, index, request);
        final var view = this.removeOptionUseCase.removeOption(command);
        return ResponseEntity.ok(this.sharedMapper.toResponse(view));
    }
}
