package vn.uit.edu.msshop.image.application.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.image.domain.event.ImageRemoveSuccess;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;

@Component
public class ImageEventMapper {
    public ImageRemoveSuccess toEvent(ImageInfo info, UUID objectId) {
        return new ImageRemoveSuccess(info.getUrl().value(), info.getPublicId().value(), info.getFileName().value(), info.getWidth().value(), info.getHeight().value(), info.getSize().value(), objectId);
    }

}
