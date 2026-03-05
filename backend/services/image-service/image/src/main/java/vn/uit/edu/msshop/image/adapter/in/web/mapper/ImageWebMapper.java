package vn.uit.edu.msshop.image.adapter.in.web.mapper;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import vn.uit.edu.msshop.image.adapter.in.web.response.ImageResponse;
import vn.uit.edu.msshop.image.application.dto.command.DeleteImageCommand;
import vn.uit.edu.msshop.image.application.dto.command.UploadImageCommand;
import vn.uit.edu.msshop.image.domain.model.valueobject.DataType;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageFileName;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;
import vn.uit.edu.msshop.image.domain.model.valueobject.ObjectId;

@Component
public class ImageWebMapper {
    public UploadImageCommand toCommand(MultipartFile file, UUID objectId, String dataType) throws IOException {
        byte[] bytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        return new UploadImageCommand(new ImageFileName(fileName), new ObjectId(objectId), new DataType(dataType), bytes);
    }
    public DeleteImageCommand toCommand(String publicId, UUID objectId, String dataType) {
        return new DeleteImageCommand(new ImagePublicId(publicId), new ObjectId(objectId), new DataType(dataType));
    }

    public ImageResponse toResponse(String url,
    String publicId,
    String fileName,
    int width,
    int height,
    UUID objectId,
    String dataType) {
        return new ImageResponse(url, publicId, fileName, width, height, objectId, dataType);
    }
}
