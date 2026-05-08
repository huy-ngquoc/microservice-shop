package vn.uit.edu.msshop.account.application.port.in;


import vn.uit.edu.msshop.account.application.dto.command.UpdateAccountCommand;

public interface UpdateAccountUseCase {
    public void update(UpdateAccountCommand updateAccountCommand);
}
