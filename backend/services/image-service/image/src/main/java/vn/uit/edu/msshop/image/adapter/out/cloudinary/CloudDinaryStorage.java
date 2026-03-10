package vn.uit.edu.msshop.image.adapter.out.cloudinary;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;

import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.image.application.dto.command.RemoveAvatarImageCommand;
import vn.uit.edu.msshop.image.application.dto.command.RemoveImageFolderCommand;
import vn.uit.edu.msshop.image.application.dto.command.RemoveProductImageCommand;
import vn.uit.edu.msshop.image.application.dto.command.RemoveVariantImageCommand;
import vn.uit.edu.msshop.image.application.port.out.DeleteImagePort;
import vn.uit.edu.msshop.image.application.port.out.GetSignaturePort;
import vn.uit.edu.msshop.image.application.port.out.RemoveImageFolderPort;
import vn.uit.edu.msshop.image.domain.event.ImageRemoveFail;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;
import vn.uit.edu.msshop.image.domain.model.valueobject.DataType;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageFileName;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageHeight;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageSize;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageUrl;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageWidth;
import vn.uit.edu.msshop.image.domain.model.valueobject.ObjectId;
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
    public void deleteImage(ImagePublicId publicId, ObjectId objectId, DataType dataType) {
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
    public ImageInfo remove(RemoveImageFolderCommand command) {
        String newFolder = getFolderNameFromDataType(command);
        String fileName = command.getPublicId().value().substring(command.getPublicId().value().lastIndexOf("/") + 1);
        String newPublicId = newFolder + "/" + fileName;
        try {
            Map response = this.uploader.rename(command.getPublicId().value(), newPublicId, ObjectUtils.emptyMap());
            final var url = (String) response.get(CloudDinaryResultKeys.SECURE_URL);
            final var publicId = (String) response.get(CloudDinaryResultKeys.PUBLIC_ID);
            final var size = (int) response.get(CloudDinaryResultKeys.BYTES);
            final var width = (int) response.get("width");
            final var height = (int) response.get("height");
            return new ImageInfo(new ImageUrl(url), new ImagePublicId(publicId), new ImageFileName(fileName), new ImageWidth(width), new ImageHeight(height), new ImageSize(size));
        } catch (IOException ex) {
            System.getLogger(CloudDinaryStorage.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            ImageRemoveFail removeFail = new ImageRemoveFail(command.getPublicId().value());
            Message<ImageRemoveFail> message = MessageBuilder.withPayload(removeFail).setHeader(KafkaHeaders.TOPIC, "image-topic").build();
            kafkaTemplate.send(message);

        }
        return null;
    }   
    private String getFolderNameFromDataType(RemoveImageFolderCommand command) {
        if(command instanceof RemoveAvatarImageCommand) {
            return "Avatar";
        } 
        if(command instanceof RemoveProductImageCommand) {
            return "Product";
        } 
        if(command instanceof RemoveVariantImageCommand) {
            return "Variant";
        } 
        throw new IllegalArgumentException("Invalid command");
    }

}
