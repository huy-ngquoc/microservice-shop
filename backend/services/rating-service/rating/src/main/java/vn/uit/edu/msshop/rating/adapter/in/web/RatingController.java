package vn.uit.edu.msshop.rating.adapter.in.web;

import java.io.IOException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.adapter.in.web.mapper.RatingWebMapper;
import vn.uit.edu.msshop.rating.adapter.in.web.request.PostRatingRequest;
import vn.uit.edu.msshop.rating.adapter.in.web.request.UpdateRatingRequest;
import vn.uit.edu.msshop.rating.adapter.in.web.response.ImageViewResponse;
import vn.uit.edu.msshop.rating.adapter.in.web.response.RatingResponse;
import vn.uit.edu.msshop.rating.application.dto.command.UploadRatingImageCommand;
import vn.uit.edu.msshop.rating.application.service.DeleteImageService;
import vn.uit.edu.msshop.rating.application.service.DeleteRatingService;
import vn.uit.edu.msshop.rating.application.service.FindRatingService;
import vn.uit.edu.msshop.rating.application.service.PostRatingService;
import vn.uit.edu.msshop.rating.application.service.UpdateRatingService;
import vn.uit.edu.msshop.rating.application.service.UploadImageService;
import vn.uit.edu.msshop.rating.domain.model.valueobject.MediaPublicId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {
    private final PostRatingService postService;
    private final FindRatingService findService;
    private final UpdateRatingService updateService;
    private final DeleteRatingService deleteService;
    private final RatingWebMapper mapper;
    private final UploadImageService uploadImageService;
    private final DeleteImageService deleteImageService;

    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> findById(@PathVariable UUID id) {
        final var view = findService.findById(new RatingId(id));
        return ResponseEntity.ok(this.mapper.toResponse(view));
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<RatingResponse>> findByProductId(@PathVariable UUID productId, @RequestParam(defaultValue="7") int pageSize, @RequestParam(defaultValue="0") int pageNumber) {
        final var viewPage = findService.findByProductId(new ProductId(productId), pageSize, pageNumber);
        return ResponseEntity.ok(viewPage.map(this.mapper::toResponse));
    }
    @PostMapping("/create")
    public ResponseEntity<Void> postRating(@RequestBody PostRatingRequest request) {
        postService.post(mapper.toCommand(request));
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/update") 
    public ResponseEntity<Void> updateRating(@RequestBody UpdateRatingRequest request) {
        updateService.update(mapper.toCommand(request));
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable UUID id) {
        deleteService.delete(new RatingId(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value="/upload_image/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageViewResponse> uploadImage(@PathVariable UUID id,@RequestPart("file") final MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        final String contentType = file.getContentType();
        if ((contentType == null) || (!contentType.startsWith("image/"))) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
        final var uploadImageCommand = new UploadRatingImageCommand(new RatingId(id),file.getBytes(),file.getOriginalFilename(),file.getContentType());
        final var imageView=uploadImageService.uploadRatingImage(uploadImageCommand);
        final var response = new ImageViewResponse(imageView.url(),imageView.publicId(),imageView.mediaType(),imageView.size());
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/image/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable String id) {
        deleteImageService.delete(new MediaPublicId(id));
        return ResponseEntity.noContent().build();
    }
}
