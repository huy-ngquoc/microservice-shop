package vn.uit.edu.msshop.image.adapter.in.web.mapper;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.adapter.in.web.request.RemoveImageRequest;
import vn.uit.edu.msshop.image.adapter.in.web.response.ImageResponse;
import vn.uit.edu.msshop.image.adapter.in.web.response.SignatureResponse;
import vn.uit.edu.msshop.image.application.dto.command.DeleteImageCommand;
import vn.uit.edu.msshop.image.application.dto.command.GetSignatureCommand;
import vn.uit.edu.msshop.image.application.dto.command.RemoveImageFolderCommand;
import vn.uit.edu.msshop.image.application.dto.command.RollbackImageFolderCommand;
import vn.uit.edu.msshop.image.application.dto.command.UploadImageCommand;
import vn.uit.edu.msshop.image.domain.event.RollbackImageEvent;
import vn.uit.edu.msshop.image.domain.model.valueobject.DataType;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageFileName;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageFolder;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;
import vn.uit.edu.msshop.image.domain.model.valueobject.ObjectId;
import vn.uit.edu.msshop.image.domain.model.valueobject.TimeStamp;

@Component
@RequiredArgsConstructor
public class ImageWebMapper {
    private final Cloudinary cloudinary;
    public UploadImageCommand toCommand(MultipartFile file, UUID objectId, String dataType) throws IOException {
        byte[] bytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        return new UploadImageCommand(new ImageFileName(fileName), new ObjectId(objectId), new DataType(dataType), bytes);
    }

    public DeleteImageCommand toCommand(String publicId) {
        return new DeleteImageCommand(new ImagePublicId(publicId));
    }
    public RollbackImageFolderCommand toCommand(RollbackImageEvent event) {
        return new RollbackImageFolderCommand(new ImagePublicId(event.imagePublicId()));
    }
    public RemoveImageFolderCommand toCommand(RollbackImageFolderCommand command) {
        return new RemoveImageFolderCommand(command.getPublicId(),new ImageFolder("temp"),null);
    }
    

    public ImageResponse toResponse(String url,
    String publicId,
    String fileName,
    int width,
    int height,
    UUID objectId,
    String dataType) {
        return new ImageResponse(url, publicId, fileName, width, height);
    }

    public GetSignatureCommand toCommand() {
        long timestamp = System.currentTimeMillis() / 1000L;
        TimeStamp ts = new TimeStamp(timestamp);
        return new GetSignatureCommand(ts);
    }
    public SignatureResponse toResponse(String signature, long timeStamp) {
        return new SignatureResponse(signature,timeStamp,cloudinary.config.apiKey,cloudinary.config.cloudName);
    } 
    public RemoveImageFolderCommand toCommand(RemoveImageRequest request) {
        return new RemoveImageFolderCommand(new ImagePublicId(request.getImagePublicId()),new ImageFolder(request.getDestination()), new ObjectId(request.getObjectId()));
    }
}
