package vn.uit.edu.msshop.image.application.dto.command;

import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;



public class RemoveProductImageCommand extends RemoveImageFolderCommand {
    public RemoveProductImageCommand(ImagePublicId id) {
        super(id);
    }
}
