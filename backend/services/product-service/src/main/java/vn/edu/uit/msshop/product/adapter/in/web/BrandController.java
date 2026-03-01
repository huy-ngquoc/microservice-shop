package vn.edu.uit.msshop.product.adapter.in.web;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.adapter.in.web.mapper.BrandWebMapper;
import vn.edu.uit.msshop.product.adapter.in.web.request.CreateBrandRequest;
import vn.edu.uit.msshop.product.adapter.in.web.request.UpdateBrandInfoRequest;
import vn.edu.uit.msshop.product.adapter.in.web.response.BrandLogoResponse;
import vn.edu.uit.msshop.product.adapter.in.web.response.BrandResponse;
import vn.edu.uit.msshop.product.application.dto.command.UpdateBrandLogoCommand;
import vn.edu.uit.msshop.product.application.port.in.CreateBrandUseCase;
import vn.edu.uit.msshop.product.application.port.in.FindBrandUseCase;
import vn.edu.uit.msshop.product.application.port.in.UpdateBrandInfoUseCase;
import vn.edu.uit.msshop.product.application.port.in.UpdateBrandLogoUseCase;
import vn.edu.uit.msshop.product.application.service.FindBrandLogoService;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {
    private final FindBrandUseCase findUseCase;
    private final FindBrandLogoService findLogoUseCase;
    private final CreateBrandUseCase createUseCase;
    private final UpdateBrandInfoUseCase updateInfoUseCase;
    private final UpdateBrandLogoUseCase updateLogoUseCase;
    private final BrandWebMapper webMapper;

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findById(new BrandId(id));
        final var response = this.webMapper.toResponse(view);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/logo")
    public ResponseEntity<BrandLogoResponse> findLogoById(
            @PathVariable
            final UUID id) {
        final var view = this.findLogoUseCase.findById(new BrandId(id));
        final var response = this.webMapper.toResponse(view);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody
            final CreateBrandRequest request) {
        final var command = this.webMapper.toCommand(request);
        this.createUseCase.create(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/info")
    public ResponseEntity<Void> updateInfo(
            @PathVariable
            final UUID id,

            @RequestBody
            final UpdateBrandInfoRequest request) {
        final var command = this.webMapper.toCommand(id, request);
        this.updateInfoUseCase.updateInfo(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(
            value = "/{id}/logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BrandLogoResponse> updateImage(
            @PathVariable
            final UUID id,

            @RequestPart("file")
            final MultipartFile file)
            throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        final var contentType = file.getContentType();
        if ((contentType == null || contentType.startsWith("image/"))) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "logo";
        }

        final var command = new UpdateBrandLogoCommand(
                new BrandId(id),
                file.getBytes(),
                originalFilename,
                contentType);

        final var view = this.updateLogoUseCase.updateImage(command);
        final var response = this.webMapper.toResponse(view);

        return ResponseEntity.ok(response);
    }
}
