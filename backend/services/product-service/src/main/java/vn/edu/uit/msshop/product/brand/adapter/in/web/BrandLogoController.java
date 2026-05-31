package vn.edu.uit.msshop.product.brand.adapter.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.in.web.mapper.BrandLogoWebMapper;
import vn.edu.uit.msshop.product.brand.adapter.in.web.mapper.BrandSharedWebMapper;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandLogoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandLogoResponse;
import vn.edu.uit.msshop.product.brand.application.port.in.command.BrandLogoLifecycleUseCases;
import vn.edu.uit.msshop.product.brand.application.port.in.query.BrandLookupUseCases;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandLogoController {

    private final BrandLookupUseCases.FindActiveLogoById findLogoUseCase;
    private final BrandLogoLifecycleUseCases.Update updateUseCase;
    private final BrandLogoLifecycleUseCases.Delete deleteUseCase;

    private final BrandLogoWebMapper mapper;
    private final BrandSharedWebMapper sharedMapper;

    @GetMapping("/{id}/logo")
    public ResponseEntity<BrandLogoResponse> findLogoById(
            @PathVariable
            final UUID id) {
        final var view = this.findLogoUseCase.findActiveLogoById(
                this.sharedMapper.toBrandId(id));

        final var response = this.sharedMapper.toLogoResponse(view);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/logo")
    public ResponseEntity<BrandLogoResponse> updateLogo(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateBrandLogoRequest request) {
        final var command = this.mapper.toUpdateLogoCommand(id, request);
        final var view = this.updateUseCase.update(command);

        final var response = this.sharedMapper.toLogoResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/logo")
    public ResponseEntity<Void> deleteLogoById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toDeleteLogoCommand(id, version);
        this.deleteUseCase.delete(command);

        return ResponseEntity.ok().build();
    }
}
