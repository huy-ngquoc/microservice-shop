package vn.edu.uit.msshop.product.variant.adapter.in.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.in.web.mapper.VariantWebMapper;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.CreateVariantRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantInfoRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantResponse;
import vn.edu.uit.msshop.product.variant.application.port.in.CreateVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.FindVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.UpdateVariantInfoUseCase;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/variants")
@RequiredArgsConstructor
public class VariantController {
    private final FindVariantUseCase findUseCase;
    private final CreateVariantUseCase createUseCase;
    private final UpdateVariantInfoUseCase updateInfoUseCase;

    private final VariantWebMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<VariantResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findById(this.mapper.toVariantId(id));
        final var response = this.mapper.toResponse(view);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody
            @Valid
            final CreateVariantRequest request) {
        final var command = this.mapper.toCommand(request);
        this.createUseCase.create(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<Void> updateInfo(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateVariantInfoRequest request) {
        final var command = this.mapper.toCommand(id, request);
        this.updateInfoUseCase.updateInfo(command);

        return ResponseEntity.noContent().build();
    }
}
