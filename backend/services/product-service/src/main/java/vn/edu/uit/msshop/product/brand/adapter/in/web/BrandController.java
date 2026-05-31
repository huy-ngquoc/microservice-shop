package vn.edu.uit.msshop.product.brand.adapter.in.web;

import java.util.UUID;

import org.jspecify.annotations.Nullable;
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
import vn.edu.uit.msshop.product.brand.application.port.in.command.BrandLifecycleUseCases;
import vn.edu.uit.msshop.product.brand.application.port.in.command.BrandLogoLifecycleUseCases;
import vn.edu.uit.msshop.product.brand.application.port.in.query.BrandLookupUseCases;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandLookupUseCases.ListActive listUseCase;
    private final BrandLookupUseCases.ListSoftDeleted listSoftDeletedUseCase;
    private final BrandLookupUseCases.FindActiveById findUseCase;
    private final BrandLookupUseCases.FindSoftDeletedById findSoftDeletedUseCase;
    private final BrandLookupUseCases.FindActiveLogoById findLogoUseCase;
    private final BrandLookupUseCases.CheckExistsById checkExistsUseCase;
    private final BrandLifecycleUseCases.Create createUseCase;
    private final BrandLifecycleUseCases.Restore restoreUseCase;
    private final BrandLifecycleUseCases.UpdateInfo updateInfoUseCase;
    private final BrandLogoLifecycleUseCases.Update updateLogoUseCase;
    private final BrandLogoLifecycleUseCases.Delete deleteLogoUseCase;
    private final BrandLifecycleUseCases.SoftDelete softDeleteUseCase;
    private final BrandLifecycleUseCases.HardDelete hardDeleteUseCase;
    private final BrandWebMapper mapper;

    @GetMapping
    public ResponseEntity<PageResponseDto<BrandResponse>> list(
            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_PAGE_STRING)
            final int page,

            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_SIZE_STRING)
            final int size,

            @RequestParam(
                    required = false)
            @Nullable
            final String sortBy,

            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_DIRECTION_STRING)
            final PageRequestDto.Direction direction) {
        final var request = new PageRequestDto(page, size, sortBy, direction);
        final var views = listUseCase.listActive(request);

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted")
    public ResponseEntity<PageResponseDto<BrandResponse>> listSoftDeleted(
            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_PAGE_STRING)
            final int page,

            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_SIZE_STRING)
            final int size,

            @RequestParam(
                    required = false)
            @Nullable
            final String sortBy,

            @RequestParam(
                    defaultValue = PageRequestDto.DEFAULT_DIRECTION_STRING)
            final PageRequestDto.Direction direction) {
        final var request = new PageRequestDto(page, size, sortBy, direction);
        final var views = listSoftDeletedUseCase.listSoftDeleted(request);

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findActiveById(this.mapper.toBrandId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<BrandResponse> findSoftDeletedById(
            @PathVariable
            final UUID id) {
        final var view = this.findSoftDeletedUseCase.findSoftDeletedById(this.mapper.toBrandId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/logo")
    public ResponseEntity<BrandLogoResponse> findLogoById(
            @PathVariable
            final UUID id) {
        final var view = this.findLogoUseCase.findActiveLogoById(this.mapper.toBrandId(id));

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
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).findById(response.id())).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restore(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toRestoreCommand(id, version);
        this.restoreUseCase.restore(command);

        return ResponseEntity.noContent().build();
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
        final var view = this.updateLogoUseCase.update(command);

        final var response = this.mapper.toLogoResponse(view);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/logo")
    public ResponseEntity<Void> deleteLogoById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toDeleteLogoCommand(id, version);
        this.deleteLogoUseCase.delete(command);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toSoftDeleteCommand(id, version);
        this.softDeleteUseCase.softDelete(command);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/purge")
    public ResponseEntity<Void> hardDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toHardDeleteCommand(id, version);
        this.hardDeleteUseCase.hardDelete(command);

        return ResponseEntity.noContent().build();
    }
}
