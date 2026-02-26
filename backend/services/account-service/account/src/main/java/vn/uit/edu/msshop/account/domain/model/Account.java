package vn.edu.uit.msshop.account.domain.model;


import vn.edu.uit.msshop.product.domain.model.valueobject.AccountEmail;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountId;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountName;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountPassword;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountRole;
import vn.edu.uit.msshop.product.domain.model.valueobject.AccountStatus;


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
}
