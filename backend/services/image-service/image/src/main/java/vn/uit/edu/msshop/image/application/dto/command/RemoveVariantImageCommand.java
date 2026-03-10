package vn.uit.edu.msshop.image.application.dto.command;

import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;

public class RemoveVariantImageCommand extends RemoveImageFolderCommand{
    public RemoveVariantImageCommand(ImagePublicId id) {super(id);}
}
