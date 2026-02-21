package vn.edu.uit.msshop.profile.application.port.in;

import vn.edu.uit.msshop.profile.application.dto.command.UpdateProfileCommand;

public interface UpdateProfileUseCase {
    void update(
            final UpdateProfileCommand command);
}
