package vn.uit.edu.msshop.image.application.port.in;

import vn.uit.edu.msshop.image.application.dto.command.RemoveAvatarImageCommand;
import vn.uit.edu.msshop.image.application.dto.command.RemoveProductImageCommand;
import vn.uit.edu.msshop.image.application.dto.command.RemoveVariantImageCommand;

public interface RemoveImageFolderUseCase {
    public void removeProduct(RemoveProductImageCommand command);
    public void removeAvatar(RemoveAvatarImageCommand command);
    public void removeVariant(RemoveVariantImageCommand command);

}
