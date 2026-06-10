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
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductResponseWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductOptionAdditionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductOptionRemovalRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.ProductOptionUpdateRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.application.port.in.command.option.ProductOptionAdditionUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.option.ProductOptionRemovalUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.option.ProductOptionUpdateUseCase;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductOptionController {

    private final ProductOptionAdditionUseCase additionUseCase;
    private final ProductOptionUpdateUseCase updateUseCase;
    private final ProductOptionRemovalUseCase removalUseCase;

    private final ProductOptionWebMapper optionMapper;
    private final ProductResponseWebMapper responseMapper;

    @PostMapping("/{id}/options")
    public ResponseEntity<ProductResponse> addOption(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final ProductOptionAdditionRequest request) {
        final var command = this.optionMapper.toAdditionCommand(id, request);
        final var view = this.additionUseCase.add(command);
        return ResponseEntity.ok(this.responseMapper.toResponse(view));
    }

    @PatchMapping("/{id}/options/{index}")
    public ResponseEntity<ProductResponse> updateOption(
            @PathVariable
            final UUID id,

            @PathVariable
            final int index,

            @RequestBody
            @Valid
            final ProductOptionUpdateRequest request) {
        final var command = this.optionMapper.toUpdateCommand(id, index, request);
        final var view = this.updateUseCase.update(command);
        return ResponseEntity.ok(this.responseMapper.toResponse(view));
    }

    @DeleteMapping("/{id}/options/{index}")
    public ResponseEntity<ProductResponse> removeOption(
            @PathVariable
            final UUID id,

            @PathVariable
            final int index,

            @RequestBody
            @Valid
            final ProductOptionRemovalRequest request) {
        final var command = this.optionMapper.toRemovalCommand(id, index, request);
        final var view = this.removalUseCase.remove(command);
        return ResponseEntity.ok(this.responseMapper.toResponse(view));
    }
}
