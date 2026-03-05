package vn.uit.edu.msshop.account.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.account.adapter.in.web.request.CreateAccountRequest;
import vn.uit.edu.msshop.account.adapter.in.web.request.UpdateAccountRequest;
import vn.uit.edu.msshop.account.adapter.in.web.request.common.ChangeRequest;
import vn.uit.edu.msshop.account.adapter.in.web.response.AccountResponse;
import vn.uit.edu.msshop.account.application.dto.command.CreateAccountCommand;
import vn.uit.edu.msshop.account.application.dto.command.UpdateAccountCommand;
import vn.uit.edu.msshop.account.application.dto.query.AccountView;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountEmail;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountName;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountPassword;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountRole;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountStatus;

@Component
public class AccountWebMapper {
    public CreateAccountCommand toCommand(CreateAccountRequest request) {
        final var id = new AccountId(request.id());
        final var name = new AccountName(request.name());
        final var email = new AccountEmail(request.email());
        final var password = new AccountPassword(request.password());
        final var role = new AccountRole(request.role());
        final var status = new AccountStatus(request.status());
        return new CreateAccountCommand(id,name,email,password,role,status);
    }
    public UpdateAccountCommand toCommand(UpdateAccountRequest request) {
        final var id = new AccountId(request.id());
        System.out.println(request.name()+"eibeiereribveribiervr");
        final var name = ChangeRequest.toChange(request.name(), AccountName::new);
        System.out.println(name+" ieriereerer");
        final var email = ChangeRequest.toChange(request.email(), AccountEmail::new);
        final var password = ChangeRequest.toChange(request.password(), AccountPassword::new);
        final var role = ChangeRequest.toChange(request.role(), AccountRole::new);
        final var status = ChangeRequest.toChange(request.status(), AccountStatus::new);
        return new UpdateAccountCommand(id,name,email,password,role,status);
    

    }
    public AccountResponse toResponse(AccountView view) {
        return new AccountResponse(view.id(),view.name(),view.email(),view.password(),view.role(),view.status());
    }
}
 