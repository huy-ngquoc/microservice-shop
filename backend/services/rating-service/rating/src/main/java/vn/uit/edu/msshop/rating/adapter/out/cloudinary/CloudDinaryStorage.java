package vn.uit.edu.msshop.rating.adapter.out.cloudinary;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.rating.adapter.exception.ImageUploadFailException;
import vn.uit.edu.msshop.rating.application.port.out.DeleteRatingImagePort;
import vn.uit.edu.msshop.rating.application.port.out.UploadRatingImagePort;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Media;
import vn.uit.edu.msshop.rating.domain.model.valueobject.MediaPublicId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;
@Component
@Slf4j
public class CloudDinaryStorage implements UploadRatingImagePort,DeleteRatingImagePort {
    private static final Entry<String, String> UPLOAD_OPTION_RESOURCE_TYPE = Map
            .entry(CloudDinaryOptionKeys.RESOURCE_TYPE, CloudDinaryResourceType.IMAGE.value());
    private final Uploader uploader;

    public CloudDinaryStorage(
            final Cloudinary cloudinary) {
        this.uploader = cloudinary.uploader();
    }

    @Override
    public Media upload(RatingId ratingId, byte[] bytes, String originalFileName, String contentType) {
         try {
            final var uploadOptions = Map.<String, String>ofEntries(
                    Map.entry(CloudDinaryOptionKeys.ASSET_FOLDER, "rating/" + ratingId.value()),
                    UPLOAD_OPTION_RESOURCE_TYPE);

            final var response = this.uploader.upload(bytes, uploadOptions);

            final var url = (String) response.get(CloudDinaryResultKeys.SECURE_URL);
            final var publicId = (String) response.get(CloudDinaryResultKeys.PUBLIC_ID);
            final var size = (int) response.get(CloudDinaryResultKeys.BYTES);
            

            return new Media("IMAGE",url,size,publicId);
        } catch (final IOException e) {
            throw new ImageUploadFailException(e);
        }
    }

    @Override
    public void deleteRatingImage(MediaPublicId publicId) {
        try {
            this.uploader.destroy(publicId.value(), Collections.emptyMap());
        } catch (final Exception e) {
            log.warn("Cannot delete avatar with public id: " + publicId.value(), e);
        }
    }

}
