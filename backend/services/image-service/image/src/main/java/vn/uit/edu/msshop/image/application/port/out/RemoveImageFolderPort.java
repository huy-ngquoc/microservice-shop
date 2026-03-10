package vn.uit.edu.msshop.image.application.port.out;

import vn.uit.edu.msshop.image.application.dto.command.RemoveImageFolderCommand;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;

public interface RemoveImageFolderPort {
    public ImageInfo remove(RemoveImageFolderCommand command);
}
