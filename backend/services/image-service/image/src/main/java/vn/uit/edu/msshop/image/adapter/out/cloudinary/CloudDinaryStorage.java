package vn.uit.edu.msshop.image.adapter.out.cloudinary;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;

import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.image.application.dto.command.RemoveImageFolderCommand;
import vn.uit.edu.msshop.image.application.port.out.DeleteImagePort;
import vn.uit.edu.msshop.image.application.port.out.GetSignaturePort;
import vn.uit.edu.msshop.image.application.port.out.RemoveImageFolderPort;
import vn.uit.edu.msshop.image.domain.event.ImageRemoveFail;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageFileName;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageHeight;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageSize;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageUrl;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageWidth;
import vn.uit.edu.msshop.image.domain.model.valueobject.TimeStamp;
@Component
@Slf4j
public class CloudDinaryStorage implements DeleteImagePort,GetSignaturePort,RemoveImageFolderPort {
    private static final Entry<String, String> UPLOAD_OPTION_RESOURCE_TYPE = Map
            .entry(CloudDinaryOptionKeys.RESOURCE_TYPE, CloudDinaryResourceType.IMAGE.value());
    private final Uploader uploader;
    private final Cloudinary cloudinary;
    private KafkaTemplate<String,ImageRemoveFail> kafkaTemplate;
    public CloudDinaryStorage(
            final Cloudinary cloudinary, KafkaTemplate<String,ImageRemoveFail> kafkaTemplate) {
        this.cloudinary = cloudinary;
        this.uploader = cloudinary.uploader();
        this.kafkaTemplate = kafkaTemplate;
    }
    
    
    @Override
    public void deleteImage(ImagePublicId publicId) {
        try {
            this.uploader.destroy(publicId.value(), Collections.emptyMap());
        } catch (final Exception e) {
            log.warn("Cannot delete image with public id: " + publicId.value(), e);
        }
    }
    @Override
    public String getSignature( TimeStamp timeStamp) {
        Map<String, Object> params = new HashMap<>();
        params.put("folder", "temp");
        params.put("timestamp", timeStamp.value());
        String signature = cloudinary.apiSignRequest(params, cloudinary.config.apiSecret,1);
        return signature;
    }

    @Override
    public ImageInfo remove(RemoveImageFolderCommand command) throws IOException {
        String fileName = command.getPublicId().value().contains("/") 
                          ? command.getPublicId().value().substring(command.getPublicId().value().lastIndexOf("/") + 1) 
                          : command.getPublicId().value();
        String newPublicId = command.getDestination().value() + "/" + fileName;

        Map options = ObjectUtils.asMap(
            "overwrite", true,      
            "invalidate", true      
        );
        final var result=this.uploader.rename(command.getPublicId().value(), newPublicId, options);
        String publicId = (String) result.get("public_id");
        String url = (String) result.get("secure_url");
        Integer width = (Integer) result.get("width");
        Integer height = (Integer) result.get("height");
        Long sizeInBytes = Long.valueOf(result.get("bytes").toString());
        return new ImageInfo(new ImageUrl(url), new ImagePublicId(publicId), new ImageFileName(fileName), new ImageWidth(width), new ImageHeight(height), new ImageSize(sizeInBytes));

    }

   

}
