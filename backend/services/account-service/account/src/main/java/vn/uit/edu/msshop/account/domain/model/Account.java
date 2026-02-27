package vn.uit.edu.msshop.account.domain.model;


import java.util.Objects;

import org.antlr.v4.runtime.misc.NotNull;
import org.jspecify.annotations.NullMarked;

import lombok.Builder;
import reactor.core.scheduler.Schedulers;
import vn.uit.edu.msshop.account.domain.model.valueobject.*;

@Builder
public class Account {
    private AccountId id;
    private AccountEmail email;
    private AccountPassword password;
    private AccountName name;
    private AccountRole role;
    private AccountStatus status;
    
    public Account() {

    } 
    public Account(AccountId id, AccountEmail email,AccountPassword password, AccountRole role, AccountStatus status, AccountName name) {
        this.id = id;
        this.email=email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.name= name;
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

    @Builder
    public static record Draft(
        @NotNull
        AccountId id,
        AccountName name,
        AccountEmail email,
        AccountPassword password,
        AccountRole role,
        AccountStatus status
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
        AccountStatus status
    ) {

    }
    @Builder
    public static record SnapShot(@NotNull
        AccountId id,
        AccountName name,
        AccountEmail email,
        AccountPassword password,
        AccountRole role,
        AccountStatus status) {

    }

    @NullMarked
    public static Account create(Draft d) {
        if(d==null) {
            throw new IllegalArgumentException("Draft must not be null");
        }
        if(d.id()==null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        return Account.builder().id(d.id()).name(d.name()).email(d.email()).password(d.password()).role(d.role()).status(d.status()).build();
    }
    @NullMarked
    public static Account reconstitue(SnapShot s) {
        if(s==null) {
            throw new IllegalArgumentException("Draft must not be null");
        }
        if(s.id()==null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        return Account.builder().id(s.id()).name(s.name()).email(s.email()).password(s.password()).role(s.role()).status(s.status()).build();
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
        return Account.builder().id(this.id).name(u.name()).email(u.email()).password(u.password()).role(u.role()).status(u.status()).build();


    }
    @NullMarked
    public SnapShot snapShot() {
        return SnapShot.builder().id(this.id).name(this.name).email(this.email).password(this.password).role(this.role).status(this.status).build();
    }
    @NullMarked
    private  boolean isSameInfoWithUpdateInfo(UpdateInfo u) {
        return Objects.equals(u.name(), this.name)&&Objects.equals(u.email(), this.email)&&Objects.equals(u.password(), this.password)&&Objects.equals(u.role(), this.role)&&Objects.equals(u.status(),this.status);
    }
}
