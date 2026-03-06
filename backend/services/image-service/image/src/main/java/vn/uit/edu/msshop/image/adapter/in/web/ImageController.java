package vn.uit.edu.msshop.image.adapter.in.web;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.adapter.in.web.mapper.ImageWebMapper;
import vn.uit.edu.msshop.image.adapter.in.web.request.GetSignatureRequest;
import vn.uit.edu.msshop.image.adapter.in.web.response.ImageResponse;
import vn.uit.edu.msshop.image.adapter.in.web.response.SignatureResponse;
import vn.uit.edu.msshop.image.application.port.in.DeleteImageUseCase;
import vn.uit.edu.msshop.image.application.port.in.GetSignatureUseCase;
import vn.uit.edu.msshop.image.application.port.in.UploadImageUseCase;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;
/*public ImageResponse toResponse(String url,
    String publicId,
    String fileName,
    int width,
    int height,
    UUID objectId,
    String dataType) */

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final UploadImageUseCase uploadUseCase;
    private final DeleteImageUseCase deleteUseCase;
    private final ImageWebMapper mapper;
    private final GetSignatureUseCase getSignatureUseCase;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam(required=true) UUID objectId, @RequestParam(required=true) String dataType, @RequestPart("file") final MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        final String contentType = file.getContentType();
        if ((contentType == null) || (!contentType.startsWith("image/"))) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
        final var uploadImageCommand = mapper.toCommand(file, objectId, dataType);
        ImageInfo imageInfo = uploadUseCase.uploadImage(uploadImageCommand);
        ImageResponse response = mapper.toResponse(imageInfo.getUrl().value(), imageInfo.getPublicId().value(), imageInfo.getFileName().value(), imageInfo.getWidth().value(), imageInfo.getHeight().value(), imageInfo.getObjectId().value(), imageInfo.getDataType().value());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteImage(@RequestParam(required=true) UUID objectId, @RequestParam(required=true) String dataType, @RequestParam(required=true) String imagePublicId) {
        final var deleteCommand = mapper.toCommand(imagePublicId, objectId, dataType);
        deleteUseCase.deleteImage(deleteCommand);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signature")
    public ResponseEntity<SignatureResponse> getSignature(@RequestBody GetSignatureRequest request) {
        final var getSignatureCommand = mapper.toCommand(request);
        final var result= getSignatureUseCase.getSignature(getSignatureCommand);
        return ResponseEntity.ok(result);
    }
}
