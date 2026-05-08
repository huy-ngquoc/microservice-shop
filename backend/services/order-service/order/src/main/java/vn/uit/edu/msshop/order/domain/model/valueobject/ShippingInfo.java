package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ShippingInfo(String fullName, String address, String phone, String email) {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?^.`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public ShippingInfo {
        if(fullName==null||fullName.isBlank()) {
            throw new IllegalArgumentException("Invalid full name");
        } 
        if(address==null||address.isBlank()) {
            throw new IllegalArgumentException("Invalid address");
        } 
        if(phone==null||phone.isBlank()) {
            throw new IllegalArgumentException("Invalid phone");
        }
        if(email==null) {
            throw new IllegalArgumentException("Invalid email");
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

}
