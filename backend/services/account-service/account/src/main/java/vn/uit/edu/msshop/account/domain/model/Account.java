package vn.uit.edu.msshop.account.domain.model;


import java.util.Objects;

import org.antlr.v4.runtime.misc.NotNull;
import org.jspecify.annotations.NullMarked;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountEmail;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountName;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountPassword;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountRole;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountStatus;
import vn.uit.edu.msshop.account.domain.model.valueobject.Avatar;
import vn.uit.edu.msshop.account.domain.model.valueobject.KeyCloakId;
import vn.uit.edu.msshop.account.domain.model.valueobject.PhoneNumber;
import vn.uit.edu.msshop.account.domain.model.valueobject.ShippingAddress;


@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Account {
    private AccountId id;
    private AccountEmail email;
    private AccountPassword password;
    private AccountName name;
    private AccountRole role;
    private AccountStatus status;
    private Avatar avatar;
    private ShippingAddress shippingAddress;
    private PhoneNumber phoneNumber;
    private KeyCloakId keyCloakId;
    
    public Account() {

    } 
   
    public AccountId getId() {
        return this.id;
    } 
    public AccountEmail getEmail() {
        return this.email;
    } 
    public AccountPassword getPassword() {
        return this.password;
    } 
    public AccountRole getRole() {
        return this.role;
    } 
    public AccountStatus getStatus() {
        return this.status;
    }
    public AccountName getName() {
        return this.name;
    } 
    public void setId(AccountId id) {
        this.id = id;
    } 
    public void setEmail(AccountEmail email) {
        this.email = email;
    } 
    public void setPassword(AccountPassword password) {
        this.password = password;
    } 
    public void setRole(AccountRole role) {
        this.role = role;
    } 
    public void setStatus(AccountStatus status) {
        this.status = status;
    } 
    public void setName(AccountName name) {
        this.name = name;
    }
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
    public KeyCloakId getKeyCloakId() {
        return this.keyCloakId;
    }
    public void setKeyCloakId(KeyCloakId keyCloakId) {
        this.keyCloakId=keyCloakId;
    }

    @Builder
    public static record Draft(
        @NotNull
        AccountId id,
        AccountName name,
        AccountEmail email,
        AccountPassword password,
        AccountRole role,
        AccountStatus status,
        PhoneNumber phoneNumber,
        ShippingAddress shippingAddress
    ) {

    }
    @Builder
    public static record UpdateInfo(
        @NotNull
        AccountId id,
        AccountName name,
        AccountEmail email,
        AccountPassword password,
        AccountRole role,
        AccountStatus status,
        ShippingAddress shippingAddress,
        PhoneNumber phoneNumber
    ) {

    }
    @Builder
    public static record SnapShot(@NotNull
        AccountId id,
        AccountName name,
        AccountEmail email,
        AccountPassword password,
        AccountRole role,
        AccountStatus status,
        Avatar avatar,
        ShippingAddress shippingAdress,
        PhoneNumber phoneNumber,
        KeyCloakId keyCloakId
    ) {

    }

    @NullMarked
    public static Account create(Draft d) {
        if(d==null) {
            throw new IllegalArgumentException("Draft must not be null");
        }
        if(d.id()==null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        return Account.builder().id(d.id()).name(d.name()).email(d.email()).password(d.password()).role(d.role()).status(d.status()).avatar(null).shippingAddress(d.shippingAddress()).phoneNumber(d.phoneNumber())
        .keyCloakId(new KeyCloakId(null))
        .build();
    }
    @NullMarked
    public static Account reconstitue(SnapShot s) {
        if(s==null) {
            throw new IllegalArgumentException("Draft must not be null");
        }
        if(s.id()==null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        return Account.builder().id(s.id()).name(s.name()).email(s.email()).password(s.password()).role(s.role()).status(s.status()).avatar(s.avatar()).shippingAddress(s.shippingAdress()).phoneNumber(s.phoneNumber())
        .keyCloakId(s.keyCloakId())
        .build();
    }
    @NullMarked
    public Account applyUpdateInfo(UpdateInfo u) {
        if(u==null) {
            throw new IllegalArgumentException("Update info must not be null");
        }
        if(u.id()==null) {
            throw new IllegalArgumentException("Id must not be null");
            
        }
        if(this.isSameInfoWithUpdateInfo(u)) {
            return this;
        } 
        return Account.builder().id(this.id).name(u.name()).email(u.email()).password(u.password()).role(u.role()).status(u.status()).avatar(this.avatar).shippingAddress(u.shippingAddress()).phoneNumber(u.phoneNumber())
        .keyCloakId(this.keyCloakId)
        .build();


    }
    @NullMarked
    public SnapShot snapShot() {
        return SnapShot.builder().id(this.id).name(this.name).email(this.email).password(this.password).role(this.role).status(this.status).avatar(this.avatar).shippingAdress(this.shippingAddress).phoneNumber(this.phoneNumber)
        .keyCloakId(this.keyCloakId)
        .build();
    }
    @NullMarked
    private  boolean isSameInfoWithUpdateInfo(UpdateInfo u) {
        return Objects.equals(u.name(), this.name)&&Objects.equals(u.email(), this.email)&&Objects.equals(u.password(), this.password)&&Objects.equals(u.role(), this.role)&&Objects.equals(u.status(),this.status)&&Objects.equals(u.shippingAddress(),this.shippingAddress)&&Objects.equals(u.phoneNumber(),this.phoneNumber);
    }
}
