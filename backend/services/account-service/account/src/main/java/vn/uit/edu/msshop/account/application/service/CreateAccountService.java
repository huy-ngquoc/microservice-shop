package vn.uit.edu.msshop.account.application.service;



import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.adapter.out.persistence.AccountOutboxEntity;
import vn.uit.edu.msshop.account.adapter.out.persistence.AccountOutboxEntityRepository;
import vn.uit.edu.msshop.account.adapter.out.persistence.mapper.AccountEntityMapper;
import vn.uit.edu.msshop.account.application.dto.command.CreateAccountCommand;
import vn.uit.edu.msshop.account.application.dto.query.AccountView;
import vn.uit.edu.msshop.account.application.mapper.AccountViewMapper;
import vn.uit.edu.msshop.account.application.port.in.CreateAccountUseCase;
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
    
    private final AccountViewMapper mapper;
    private final AccountOutboxEntityRepository accountOutboxEntityRepo;
    private final AccountEntityMapper entityMapper;
    /*
    private String userName;
    private String password;
    private String userRole;
    private String userEmail;
    private boolean isCheck;
    private Instant createdAt;
    private Instant updatedAt;
    private int retryCount;
    private String lastError; */
    @Override
    @Transactional
    public AccountView create(CreateAccountCommand createAccountCommand) {
        String userId = UUID.randomUUID().toString();
        
        

        


        final var draft = Account.Draft.builder().id(new AccountId(UUID.fromString(userId))).name(createAccountCommand.name())
        .email(createAccountCommand.email()).password(createAccountCommand.password()).role(createAccountCommand.role()).status(new AccountStatus("PENDING")).
        shippingAddress(createAccountCommand.shippingAddress()).phoneNumber(createAccountCommand.phoneNumber()).build();
        
        final var account = Account.create(draft);
        final var saved = this.savePort.saveAndReturnJpa(account);
        final var outboxEntity = AccountOutboxEntity.builder().account(saved)
        .userName(saved.getName())
        .userEmail(saved.getEmail())
        .password(createAccountCommand.password().value())
        .userRole(createAccountCommand.role().value())
        .firstName(createAccountCommand.firstName().value())
        .lastName(createAccountCommand.lastName().value())
        .isCheck(false)
        .createdAt(Instant.now())
        .updatedAt(null)
        .retryCount(0)
        .lastError(null).build();
        accountOutboxEntityRepo.save(outboxEntity);

        this.eventPort.publish(new AccountCreated(new AccountId(saved.getId())));
        
        return mapper.toView(entityMapper.toDomain(saved));
    }
    

}
