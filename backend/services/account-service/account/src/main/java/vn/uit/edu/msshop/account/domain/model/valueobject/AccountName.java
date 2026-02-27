package vn.uit.edu.msshop.account.domain.model.valueobject;

import java.util.regex.Pattern;

public record AccountName (String value) {
     private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{IsWhite_Space}+");
     public AccountName {
        if(value==null) {
            throw new IllegalArgumentException("Account name is null");
        }
        value = WHITESPACE_PATTERN.matcher(value.trim()).replaceAll(" ");

        if (value.isBlank()) {
            throw new IllegalArgumentException("accountName blank");
        }
        if (value.length() > 80) {
            throw new IllegalArgumentException("accountName too long");
        }
     }
}
