package vn.edu.uit.msshop.product.brand.adapter.in.web;

import java.util.UUID;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.in.web.mapper.BrandWebMapper;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.CreateBrandRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandInfoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.request.UpdateBrandLogoRequest;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandLogoResponse;
import vn.edu.uit.msshop.product.brand.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.brand.application.port.in.command.CreateBrandUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.command.DeleteBrandLogoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.command.UpdateBrandInfoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.command.UpdateBrandLogoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.query.CheckBrandExistsUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.query.FindBrandLogoUseCase;
import vn.edu.uit.msshop.product.brand.application.port.in.query.FindBrandUseCase;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {
    private final FindBrandUseCase findUseCase;
    private final FindBrandLogoUseCase findLogoUseCase;
    private final CheckBrandExistsUseCase checkExistsUseCase;
    private final CreateBrandUseCase createUseCase;
    private final UpdateBrandInfoUseCase updateInfoUseCase;
    private final UpdateBrandLogoUseCase updateLogoUseCase;
    private final DeleteBrandLogoUseCase deleteLogoUseCase;
    private final BrandWebMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findById(this.mapper.toBrandId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/logo")
    public ResponseEntity<BrandLogoResponse> findLogoById(
            @PathVariable
            final UUID id) {
        final var view = this.findLogoUseCase.findLogoById(this.mapper.toBrandId(id));

        final var response = this.mapper.toLogoResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> existsById(
            @PathVariable
            final UUID id) {
        final var existed = this.checkExistsUseCase.existsById(this.mapper.toBrandId(id));
        if (!existed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<BrandResponse> create(
            @RequestBody
            @Valid
            final CreateBrandRequest request) {
        final var command = this.mapper.toCreateCommand(request);
        final var view = this.createUseCase.create(command);

        final var response = this.mapper.toResponse(view);
        final var location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).findById(response.id()))
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<BrandResponse> updateInfo(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final UpdateBrandInfoRequest request) {
        final var command = this.mapper.toUpdateInfoCommand(id, request);
        final var view = this.updateInfoUseCase.updateInfo(command);

        final var response = this.mapper.toResponse(view);
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
        final var view = this.updateLogoUseCase.updateLogo(command);

        final var response = this.mapper.toLogoResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/logo")
    public ResponseEntity<BrandLogoResponse> deleteLogoById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toDeleteLogoCommand(id, version);
        final var view = this.deleteLogoUseCase.deleteLogo(command);

        final var response = this.mapper.toLogoResponse(view);
        return ResponseEntity.ok(response);
    }
}
