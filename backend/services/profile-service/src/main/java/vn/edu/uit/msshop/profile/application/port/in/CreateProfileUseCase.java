package vn.edu.uit.msshop.profile.application.port.in;

import vn.edu.uit.msshop.profile.application.dto.command.CreateProfileCommand;

public interface CreateProfileUseCase {
    void create(
            final CreateProfileCommand command);
}
