package vn.uit.edu.msshop.image.application.port.out;

import java.io.IOException;

import vn.uit.edu.msshop.image.application.dto.command.RemoveImageFolderCommand;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;

public interface RemoveImageFolderPort {
    public ImageInfo remove(RemoveImageFolderCommand command) throws IOException;
    
}
