
package vn.uit.edu.msshop.account.domain.model.valueobject;
public record AccountPassword(String value) {
    public AccountPassword {
        if(value==null) {
            throw new IllegalArgumentException("Invalid password");
        }
    }
}
