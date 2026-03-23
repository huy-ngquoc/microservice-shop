package vn.uit.edu.msshop.account.application.service;



import java.util.Collections;
import java.util.UUID;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.adapter.in.web.request.CreateAccountRequest;
import vn.uit.edu.msshop.account.application.dto.command.CreateAccountCommand;
import vn.uit.edu.msshop.account.application.port.in.CreateAccountUseCase;
import vn.uit.edu.msshop.account.application.port.out.CreateKeyCloakAccountPort;
import vn.uit.edu.msshop.account.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.account.application.port.out.SaveAccountPort;
import vn.uit.edu.msshop.account.domain.event.normal.AccountCreated;
import vn.uit.edu.msshop.account.domain.model.Account;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountStatus;
@Service
@RequiredArgsConstructor
public class CreateAccountService implements CreateAccountUseCase {
    private final SaveAccountPort savePort;
    private final PublishAccountEventPort eventPort;
    private final CreateKeyCloakAccountPort createKeyCloakPort;
    @Override
    public void create(CreateAccountCommand createAccountCommand) {
        final var userPresentation = toUserRepresentation(createAccountCommand);
        final var response = createKeyCloakPort.createAccount(userPresentation);
        final var draft = Account.Draft.builder().id(new AccountId(UUID.fromString(CreatedResponseUtil.getCreatedId(response)))).name(createAccountCommand.name())
        .email(createAccountCommand.email()).password(createAccountCommand.password()).role(createAccountCommand.role()).status(new AccountStatus("ACTIVE")).
        shippingAddress(createAccountCommand.shippingAddress()).phoneNumber(createAccountCommand.phoneNumber()).build();
        final var account = Account.create(draft);
        final var saved = this.savePort.save(account);
        this.eventPort.publish(new AccountCreated(saved.getId()));

    }
    private UserRepresentation toUserRepresentation(CreateAccountCommand command) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(command.name().value());
        user.setEmail(command.email().value());
        user.setEnabled(true);

       
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(command.password().value());
        user.setCredentials(Collections.singletonList(cred));
        return user;
    }

}
