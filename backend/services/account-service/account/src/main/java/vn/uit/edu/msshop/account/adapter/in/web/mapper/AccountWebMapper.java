package vn.uit.edu.msshop.account.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.account.adapter.in.web.request.CreateAccountRequest;
import vn.uit.edu.msshop.account.adapter.in.web.request.UpdateAccountRequest;
import vn.uit.edu.msshop.account.adapter.in.web.request.common.ChangeRequest;
import vn.uit.edu.msshop.account.adapter.in.web.response.AccountResponse;
import vn.uit.edu.msshop.account.application.dto.command.CreateAccountCommand;
import vn.uit.edu.msshop.account.application.dto.command.UpdateAccountCommand;
import vn.uit.edu.msshop.account.application.dto.command.UpdateAvatarCommand;
import vn.uit.edu.msshop.account.application.dto.query.AccountView;
import vn.uit.edu.msshop.account.domain.event.kafka.AccountCreated;
import vn.uit.edu.msshop.account.domain.event.kafka.ImageRemoveSuccess;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountEmail;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountName;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountPassword;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountRole;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountStatus;
import vn.uit.edu.msshop.account.domain.model.valueobject.AvatarPublicId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AvatarUrl;
import vn.uit.edu.msshop.account.domain.model.valueobject.ImageSize;
import vn.uit.edu.msshop.account.domain.model.valueobject.PhoneNumber;
import vn.uit.edu.msshop.account.domain.model.valueobject.ShippingAddress;

@Component
public class AccountWebMapper {
    public CreateAccountCommand toCommand(CreateAccountRequest request) {
       
        final var name = new AccountName(request.name());
        final var email = new AccountEmail(request.email());
        final var password = new AccountPassword(request.password());
        final var role = new AccountRole(request.role());
        
        final var shippingAddress = new ShippingAddress(request.shippingAddress());
        final var phoneNumber = new PhoneNumber(request.phoneNumber());
        return new CreateAccountCommand(name, email, password, shippingAddress, phoneNumber,role);
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
        final var shippingAddress = ChangeRequest.toChange(request.shippingAddress(),ShippingAddress::new);
        final var phoneNumber = ChangeRequest.toChange(request.phoneNumber(),PhoneNumber::new);
        return new UpdateAccountCommand(id,name,email,password,role,status,shippingAddress,phoneNumber);
    

    }
    
    public AccountResponse toResponse(AccountView view) {
        return new AccountResponse(view.id(),view.name(),view.email(),view.password(),view.role(),view.status(),view.avatarUrl(),view.phoneNumber(),view.shippingAddress());
    }
    public CreateAccountCommand toCommand(AccountCreated accountCreated) {
        
        final var name = new AccountName(accountCreated.name());
        final var email = new AccountEmail(accountCreated.email());
        final var password = new AccountPassword(accountCreated.password());
        final var role = new AccountRole(accountCreated.role());
        
        final var shippingAddress = new ShippingAddress(accountCreated.shippingAddress());
        final var phoneNumber = new PhoneNumber(accountCreated.phoneNumber());
        return new CreateAccountCommand(name,email,password,shippingAddress,phoneNumber,role);
    }
    public UpdateAvatarCommand toCommand(ImageRemoveSuccess event) {
        return new UpdateAvatarCommand(new AvatarUrl(event.getUrl()), new AvatarPublicId(event.getPublicId()), new ImageSize(event.getWidth(),event.getHeight()), new AccountId(event.getObjectId()));
        
    }
}
 