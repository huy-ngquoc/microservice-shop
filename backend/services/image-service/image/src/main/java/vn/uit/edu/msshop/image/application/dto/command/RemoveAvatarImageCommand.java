package vn.uit.edu.msshop.image.application.dto.command;

import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;



public class RemoveAvatarImageCommand extends RemoveImageFolderCommand{
    public RemoveAvatarImageCommand(ImagePublicId id) {
        super(id);
    }
}
