package vn.uit.edu.msshop.account.application.port.in;


import vn.uit.edu.msshop.account.application.dto.command.CreateAccountCommand;



public interface  CreateAccountUseCase {
    public void create(CreateAccountCommand createAccountCommand);

}
