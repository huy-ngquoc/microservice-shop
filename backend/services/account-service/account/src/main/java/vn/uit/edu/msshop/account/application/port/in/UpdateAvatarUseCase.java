package vn.uit.edu.msshop.account.application.port.in;

import vn.uit.edu.msshop.account.application.dto.command.UpdateAvatarCommand;

public interface UpdateAvatarUseCase {
    public void updateAvatar(UpdateAvatarCommand command);
}
