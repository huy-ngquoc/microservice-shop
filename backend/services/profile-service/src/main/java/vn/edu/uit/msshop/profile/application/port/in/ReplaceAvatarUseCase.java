package vn.edu.uit.msshop.profile.application.port.in;

import vn.edu.uit.msshop.profile.application.dto.command.ReplaceAvatarCommand;
import vn.edu.uit.msshop.profile.application.dto.query.AvatarView;

public interface ReplaceAvatarUseCase {
    AvatarView replace(
            final ReplaceAvatarCommand command);
}
