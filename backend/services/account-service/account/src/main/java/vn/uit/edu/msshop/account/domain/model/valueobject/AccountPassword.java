
package vn.edu.uit.msshop.product.domain.model.valueobject;
public record AccountPassword(String value) {
    public AccountPassword {
        if(value==null) {
            throw new IllegalArgumentException("Invalid password");
        }
    }
}
