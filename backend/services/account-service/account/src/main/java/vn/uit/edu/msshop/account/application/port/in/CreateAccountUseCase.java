package vn.edu.uit.msshop.profile.application.port.in;

import vn.edu.uit.msshop.account.application.dto.command.CreateAccountCommand;



public interface  CreateAccountUseCase {
    public void create(CreateAccountCommand createAccountCommand);

}
