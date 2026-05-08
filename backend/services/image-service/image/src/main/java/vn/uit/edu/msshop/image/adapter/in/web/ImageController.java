package vn.uit.edu.msshop.image.adapter.in.web;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.adapter.in.web.mapper.ImageWebMapper;
import vn.uit.edu.msshop.image.adapter.in.web.request.RemoveImageRequest;
import vn.uit.edu.msshop.image.adapter.in.web.response.SignatureResponse;
import vn.uit.edu.msshop.image.application.dto.command.RemoveImageFolderCommand;
import vn.uit.edu.msshop.image.application.port.in.DeleteImageUseCase;
import vn.uit.edu.msshop.image.application.port.in.GetSignatureUseCase;
import vn.uit.edu.msshop.image.application.port.in.RemoveImageFolderUseCase;
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
    private final RemoveImageFolderUseCase removeUseCase;

    

    @DeleteMapping()
    public ResponseEntity<Void> deleteImage( @RequestParam(required=true) String imagePublicId) {
        final var deleteCommand = mapper.toCommand(imagePublicId);
        deleteUseCase.deleteImage(deleteCommand);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/signature")
    public ResponseEntity<SignatureResponse> getSignature() {
        final var getSignatureCommand = mapper.toCommand();
        final var result= getSignatureUseCase.getSignature(getSignatureCommand);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> removeImage(@RequestBody RemoveImageRequest request) throws IOException{
        RemoveImageFolderCommand command = mapper.toCommand(request);
        this.removeUseCase.removeImageFolder(command);
        return ResponseEntity.noContent().build();

    } 
}
