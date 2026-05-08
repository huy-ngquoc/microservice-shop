package vn.uit.edu.msshop.account.domain.model.valueobject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record AccountEmail(String value) {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?^.`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    public AccountEmail {
        if(value==null) {
            throw new IllegalArgumentException("Invalid email");
        } 
        Matcher matcher = EMAIL_PATTERN.matcher(value);
        if(!matcher.matches()) {
            throw new IllegalArgumentException("Invalid email");
        }


    }
}
