package vn.uit.edu.msshop.image.adapter.out.event;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.image.domain.event.ImageRemoveSuccess;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;

@Component
public class ImageEventMapper {
    public ImageRemoveSuccess toEvent(ImageInfo info) {
        return new ImageRemoveSuccess(info.getUrl().value(), info.getPublicId().value(), info.getFileName().value(), info.getWidth().value(),info.getHeight().value(), info.getSize().value());
    }
}
