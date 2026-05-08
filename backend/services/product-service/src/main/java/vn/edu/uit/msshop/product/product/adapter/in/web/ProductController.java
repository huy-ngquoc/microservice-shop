package vn.edu.uit.msshop.product.product.adapter.in.web;

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
import vn.edu.uit.msshop.product.product.adapter.in.web.mapper.ProductWebMapper;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductVariantRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductVariantsRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateProductRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateSimpleProductRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.RemoveProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductInfoRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.application.port.in.command.AddProductOptionUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.AddProductVariantsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.CreateProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.HardDeleteProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.RemoveProductOptionUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.RestoreProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.SoftDeleteProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.UpdateProductInfoUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.UpdateProductOptionUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckProductExistsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.FindProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.FindSoftDeletedProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.ListProductsUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.query.ListSoftDeletedProductsUseCase;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ListProductsUseCase listUseCase;
    private final ListSoftDeletedProductsUseCase listSoftDeletedUseCase;
    private final FindProductUseCase findUseCase;
    private final CheckProductExistsUseCase checkExistsUseCase;
    private final FindSoftDeletedProductUseCase findSoftDeletedUseCase;
    private final CreateProductUseCase createUseCase;
    private final RestoreProductUseCase restoreUseCase;
    private final AddProductOptionUseCase addOptionUseCase;
    private final AddProductVariantsUseCase addVariantsUseCase;
    private final UpdateProductInfoUseCase updateInfoUseCase;
    private final UpdateProductOptionUseCase updateOptionUseCase;
    private final RemoveProductOptionUseCase removeOptionUseCase;
    private final SoftDeleteProductUseCase softDeleteUseCase;
    private final HardDeleteProductUseCase hardDeleteUseCase;
    private final ProductWebMapper mapper;

    // TODO: product response has too much info.
    @GetMapping
    public ResponseEntity<PageResponseDto<ProductResponse>> list(
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
        final var views = this.listUseCase.list(request);

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted")
    public ResponseEntity<PageResponseDto<ProductResponse>> listSoftDeleted(
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
        final var views = this.listSoftDeletedUseCase.listSoftDeleted(request);

        final var response = views.map(this.mapper::toResponse);
        return ResponseEntity.ok(response);
    }

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

    @GetMapping("/deleted/{id}")
    public ResponseEntity<ProductResponse> findSoftDeletedById(
            @PathVariable
            final UUID id) {
        final var view = this.findSoftDeletedUseCase.findSoftDeletedById(this.mapper.toProductId(id));

        final var response = this.mapper.toResponse(view);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @RequestBody
            @Valid
            final CreateProductRequest request) {
        final var view = this.createUseCase.create(this.mapper.toCreateCommand(request));

        final var response = this.mapper.toResponse(view);
        final var location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).findById(response.id())).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/simple")
    public ResponseEntity<ProductResponse> createSimple(
            @RequestBody
            @Valid
            final CreateSimpleProductRequest request) {
        final var view = this.createUseCase.createSimple(this.mapper.toCreateSimpleCommand(request));

        final var response = this.mapper.toResponse(view);
        final var location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).findById(response.id())).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<Void> restoreById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toRestoreCommand(id, version);
        this.restoreUseCase.restore(command);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<ProductResponse> addOption(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final AddProductOptionRequest request) {
        final var command = this.mapper.toAddOptionCommand(id, request);
        final var view = this.addOptionUseCase.addOption(command);
        return ResponseEntity.ok(this.mapper.toResponse(view));
    }

    @PostMapping("/{id}/variants")
    public ResponseEntity<ProductResponse> addVariant(
            @PathVariable
            final UUID id,

            @RequestBody
            @Valid
            final AddProductVariantRequest request) {
        final var command = this.mapper.toAddVariantsCommand(id, request);
        final var view = this.addVariantsUseCase.addVariants(command);
        return ResponseEntity.ok(this.mapper.toResponse(view));
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
        return ResponseEntity.ok(this.mapper.toResponse(view));
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
        return ResponseEntity.ok(this.mapper.toResponse(view));
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
        return ResponseEntity.ok(this.mapper.toResponse(view));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toSoftDeleteCommand(id, version);
        this.softDeleteUseCase.delete(command);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/purge")
    public ResponseEntity<Void> hardDeleteById(
            @PathVariable
            final UUID id,

            @RequestParam
            final long version) {
        final var command = this.mapper.toHardDeleteCommand(id, version);
        this.hardDeleteUseCase.purge(command);

        return ResponseEntity.noContent().build();
    }
}
