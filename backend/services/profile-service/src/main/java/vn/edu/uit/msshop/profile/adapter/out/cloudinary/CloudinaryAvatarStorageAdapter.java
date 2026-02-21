package vn.edu.uit.msshop.profile.adapter.out.cloudinary;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.profile.adapter.exception.AvatarUploadFailedException;
import vn.edu.uit.msshop.profile.application.port.out.DeleteAvatarPort;
import vn.edu.uit.msshop.profile.application.port.out.UploadAvatarPort;
import vn.edu.uit.msshop.profile.domain.model.valueobject.Avatar;
import vn.edu.uit.msshop.profile.domain.model.valueobject.AvatarPublicId;
import vn.edu.uit.msshop.profile.domain.model.valueobject.AvatarUrl;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ImageSize;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

@Component
@Slf4j
public class CloudinaryAvatarStorageAdapter
        implements UploadAvatarPort, DeleteAvatarPort {
    private static final Entry<String, String> UPLOAD_OPTION_RESOURCE_TYPE = Map
            .entry(CloudinaryOptionKeys.RESOURCE_TYPE, CloudinaryResourceType.IMAGE.value());

    private final Uploader uploader;

    public CloudinaryAvatarStorageAdapter(
            final Cloudinary cloudinary) {
        this.uploader = cloudinary.uploader();
    }

    @Override
    public Avatar upload(
            final ProfileId profileId,
            final byte[] bytes,
            final String originalFilename,
            final String contentType) {
        try {
            final var uploadOptions = Map.<String, String>ofEntries(
                    Map.entry(CloudinaryOptionKeys.ASSET_FOLDER, "profiles/" + profileId.value()),
                    UPLOAD_OPTION_RESOURCE_TYPE);

            final var response = this.uploader.upload(bytes, uploadOptions);

            final var url = (String) response.get(CloudinaryResultKeys.SECURE_URL);
            final var publicId = (String) response.get(CloudinaryResultKeys.PUBLIC_ID);
            final var width = (int) response.get(CloudinaryResultKeys.WIDTH);
            final var height = (int) response.get(CloudinaryResultKeys.HEIGHT);

            return new Avatar(
                    new AvatarUrl(url),
                    new AvatarPublicId(publicId),
                    new ImageSize(width, height));
        } catch (final IOException e) {
            throw new AvatarUploadFailedException(e);
        }
    }

    @Override
    public void deleteByPublicId(
            final AvatarPublicId publicId) {
        try {
            this.uploader.destroy(publicId.value(), Collections.emptyMap());
        } catch (final Exception e) {
            log.warn("Cannot delete avatar with public id: " + publicId.value(), e);
        }
    }
}
