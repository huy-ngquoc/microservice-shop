package vn.uit.edu.msshop.account.application.port.in;


import vn.uit.edu.msshop.account.application.dto.command.CreateAccountCommand;
import vn.uit.edu.msshop.account.application.dto.query.AccountView;



public interface  CreateAccountUseCase {
    public AccountView create(CreateAccountCommand createAccountCommand);

}
