package vn.uit.edu.msshop.account.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.adapter.out.persistence.mapper.AccountEntityMapper;
import vn.uit.edu.msshop.account.application.port.out.LoadAccountPort;
import vn.uit.edu.msshop.account.application.port.out.SaveAccountPort;
import vn.uit.edu.msshop.account.domain.model.Account;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort, SaveAccountPort {
    private final SpringDataAccountJpaRepository repository;
    private final AccountEntityMapper mapper;
    

    @Override
    public Account save(Account account) {
        final var toSave = this.mapper.toEntity(account);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }

    @Override
    public Optional<Account> loadById(AccountId id) {
       final var jpaId = id.value();
       return this.repository.findById(jpaId).map(this.mapper::toDomain);
    }

   

}
