package vn.edu.uit.msshop.profile.application.port.in;

import vn.edu.uit.msshop.account.application.dto.command.UpdateAccountCommand;

public interface UpdateAccountUseCase {
    public void update(UpdateAccountCommand updateAccountCommand);
}
