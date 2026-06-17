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
import vn.edu.uit.msshop.product.product.application.port.in.command.option.ProductOptionAdditionByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.option.ProductOptionRemovalByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.option.ProductOptionUpdateByIdUseCase;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductOptionController {

    private final ProductOptionAdditionByIdUseCase additionByIdUseCase;
    private final ProductOptionUpdateByIdUseCase updateByIdUseCase;
    private final ProductOptionRemovalByIdUseCase removalByIdUseCase;

    private final ProductOptionWebMapper optionMapper;
    private final ProductResponseWebMapper responseMapper;

    @PostMapping("/{id}/options")
    public ResponseEntity<ProductResponse> addById(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final ProductOptionAdditionRequest request) {
        final var command = this.optionMapper.toAdditionByIdCommand(id, request);
        final var view = this.additionByIdUseCase.add(command);

        final var response = this.responseMapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/options/{index}")
    public ResponseEntity<ProductResponse> updateById(
            @PathVariable
            final UUID id,

            @PathVariable
            final int index,

            @RequestBody
            @Valid
            final ProductOptionUpdateRequest request) {
        final var command = this.optionMapper.toUpdateByIdCommand(id, index, request);
        final var view = this.updateByIdUseCase.update(command);

        final var response = this.responseMapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/options/{index}")
    public ResponseEntity<ProductResponse> removeById(
            @PathVariable
            final UUID id,

            @PathVariable
            final int index,

            @RequestBody
            @Valid
            final ProductOptionRemovalRequest request) {
        final var command = this.optionMapper.toRemovalByIdCommand(id, index, request);
        final var view = this.removalByIdUseCase.remove(command);

        final var response = this.responseMapper.toResponse(view);
        return ResponseEntity.ok(response);
    }
}
