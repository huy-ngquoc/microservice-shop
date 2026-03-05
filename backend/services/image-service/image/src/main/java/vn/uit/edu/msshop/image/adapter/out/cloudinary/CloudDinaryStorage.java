package vn.uit.edu.msshop.image.adapter.out.cloudinary;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.image.adapter.exception.ImageUploadFailException;
import vn.uit.edu.msshop.image.application.port.out.DeleteImagePort;
import vn.uit.edu.msshop.image.application.port.out.UploadImagePort;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;
import vn.uit.edu.msshop.image.domain.model.valueobject.DataType;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageFileName;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageHeight;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageSize;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageUrl;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageWidth;
import vn.uit.edu.msshop.image.domain.model.valueobject.ObjectId;
@Component
@Slf4j
public class CloudDinaryStorage implements UploadImagePort,DeleteImagePort {
    private static final Entry<String, String> UPLOAD_OPTION_RESOURCE_TYPE = Map
            .entry(CloudDinaryOptionKeys.RESOURCE_TYPE, CloudDinaryResourceType.IMAGE.value());
    private final Uploader uploader;

    public CloudDinaryStorage(
            final Cloudinary cloudinary) {
        this.uploader = cloudinary.uploader();
    }
    @Override
    public ImageInfo upload(ImageFileName imageFileName,ObjectId objectId, DataType dataType,byte[] bytes) {
         try {
            String folderPath = dataType.value()+"/";
            final var uploadOptions = Map.<String, String>ofEntries(
                    Map.entry(CloudDinaryOptionKeys.ASSET_FOLDER, folderPath + objectId.value()),
                    UPLOAD_OPTION_RESOURCE_TYPE);

            final var response = this.uploader.upload(bytes, uploadOptions);

            final var url = (String) response.get(CloudDinaryResultKeys.SECURE_URL);
            final var publicId = (String) response.get(CloudDinaryResultKeys.PUBLIC_ID);
            final var size = (int) response.get(CloudDinaryResultKeys.BYTES);
            final var width = (int) response.get("width");
            final var height = (int) response.get("height");

            return new ImageInfo(new ImageUrl(url), new ImagePublicId(publicId), imageFileName, new ImageWidth(width), new ImageHeight(height), new ImageSize(size), objectId, dataType);
        } catch (final IOException e) {
            throw new ImageUploadFailException(e);
        }
    }
    
    @Override
    public void deleteImage(ImagePublicId publicId, ObjectId objectId, DataType dataType) {
        try {
            this.uploader.destroy(publicId.value(), Collections.emptyMap());
        } catch (final Exception e) {
            log.warn("Cannot delete image with public id: " + publicId.value(), e);
        }
    }

}
