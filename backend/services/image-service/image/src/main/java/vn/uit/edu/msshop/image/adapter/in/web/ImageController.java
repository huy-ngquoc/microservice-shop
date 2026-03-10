package vn.uit.edu.msshop.image.adapter.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.adapter.in.web.mapper.ImageWebMapper;
import vn.uit.edu.msshop.image.adapter.in.web.response.SignatureResponse;
import vn.uit.edu.msshop.image.application.port.in.DeleteImageUseCase;
import vn.uit.edu.msshop.image.application.port.in.GetSignatureUseCase;
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
    
    private final DeleteImageUseCase deleteUseCase;
    private final ImageWebMapper mapper;
    private final GetSignatureUseCase getSignatureUseCase;

    

    @DeleteMapping()
    public ResponseEntity<Void> deleteImage(@RequestParam(required=true) UUID objectId, @RequestParam(required=true) String dataType, @RequestParam(required=true) String imagePublicId) {
        final var deleteCommand = mapper.toCommand(imagePublicId, objectId, dataType);
        deleteUseCase.deleteImage(deleteCommand);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/signature")
    public ResponseEntity<SignatureResponse> getSignature() {
        final var getSignatureCommand = mapper.toCommand();
        final var result= getSignatureUseCase.getSignature(getSignatureCommand);
        return ResponseEntity.ok(result);
    }
}
