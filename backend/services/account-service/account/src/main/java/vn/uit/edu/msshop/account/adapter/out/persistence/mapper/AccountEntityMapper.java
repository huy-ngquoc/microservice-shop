package vn.uit.edu.msshop.account.adapter.out.persistence.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.account.adapter.out.persistence.AccountJpaEntity;
import vn.uit.edu.msshop.account.domain.model.Account;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountEmail;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountName;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountPassword;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountRole;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountStatus;
import vn.uit.edu.msshop.account.domain.model.valueobject.Avatar;
import vn.uit.edu.msshop.account.domain.model.valueobject.AvatarPublicId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AvatarUrl;
import vn.uit.edu.msshop.account.domain.model.valueobject.ImageSize;
import vn.uit.edu.msshop.account.domain.model.valueobject.KeyCloakId;
import vn.uit.edu.msshop.account.domain.model.valueobject.PhoneNumber;
import vn.uit.edu.msshop.account.domain.model.valueobject.ShippingAddress;
//String avatarPublicId, int avatarWidth, int avatarHeight, String shippingAddress, String phoneNumber
@Component
public class AccountEntityMapper {
    public Account toDomain(AccountJpaEntity e) {
        
        final var snapshot = Account.SnapShot.builder().id(new AccountId(e.getId()))
        .name(new AccountName(e.getName()))
        .email(new AccountEmail(e.getEmail()))
        .password(new AccountPassword(e.getPassword()))
        .role(new AccountRole(e.getRole()))
        .status(new AccountStatus(e.getStatus()))
        .shippingAdress(new ShippingAddress(e.getShippingAddress()))
        .phoneNumber(new PhoneNumber(e.getPhoneNumber()))
        .avatar(toAvatar(e.getAvatarUrl(), e.getAvatarPublicId(), e.getAvatarWidth(), e.getAvatarHeight()))
        .keyCloakId(new KeyCloakId(e.getKeycloakId()==null?null:UUID.fromString(e.getKeycloakId())))
        .build();

        return Account.reconstitue(snapshot);
    }
    public AccountJpaEntity toEntity(Account a) {
        final var snapshot = a.snapShot();
        String url = snapshot.avatar()==null?"": snapshot.avatar().url().value();
        String publicId = snapshot.avatar()==null?"":snapshot.avatar().publicId().value();
        int width = snapshot.avatar()==null?0:snapshot.avatar().imageSize().width();
        int height = snapshot.avatar()==null?0:snapshot.avatar().imageSize().height();
        String keyCloakId=snapshot.keyCloakId().value()==null?null:snapshot.keyCloakId().value().toString();
        return AccountJpaEntity.of(snapshot.id().value(), snapshot.name().value(), snapshot.password().value(), snapshot.email().value(), snapshot.role().value(), snapshot.status().value(), url
    ,publicId,width,height, snapshot.shippingAdress().value(),snapshot.phoneNumber().value(), keyCloakId);
    }
    public Avatar toAvatar(String url, String publicId, int width, int height) {
        return new Avatar(new AvatarPublicId(publicId==null?"":publicId),new AvatarUrl(url==null?"":url),new ImageSize(width,height));
    }
}
