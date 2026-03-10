package vn.uit.edu.msshop.account.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.account.adapter.out.persistence.AccountJpaEntity;
import vn.uit.edu.msshop.account.domain.model.Account;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountEmail;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountName;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountPassword;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountRole;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountStatus;
//String avatarPublicId, int avatarWidth, int avatarHeight, String shippingAddress, String phoneNumber
@Component
public class AccountEntityMapper {
    public Account toDomain(AccountJpaEntity e) {
        final var snapshot = Account.SnapShot.builder().id(new AccountId(e.getId()))
        .name(new AccountName(e.getName()))
        .email(new AccountEmail(e.getEmail()))
        .password(new AccountPassword(e.getPassword()))
        .role(new AccountRole(e.getRole()))
        .status(new AccountStatus(e.getStatus())).build();
        return Account.reconstitue(snapshot);
    }
    public AccountJpaEntity toEntity(Account a) {
        final var snapshot = a.snapShot();
        
        return AccountJpaEntity.of(snapshot.id().value(), snapshot.name().value(), snapshot.password().value(), snapshot.email().value(), snapshot.role().value(), snapshot.status().value(), snapshot.avatar().url().value()
    ,snapshot.avatar().publicId().value(),snapshot.avatar().imageSize().width(),snapshot.avatar().imageSize().height(), snapshot.shippingAdress().value(),snapshot.phoneNumber().value());
    }
}
