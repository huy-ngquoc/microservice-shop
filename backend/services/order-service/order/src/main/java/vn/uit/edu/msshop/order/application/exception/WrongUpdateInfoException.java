package vn.uit.edu.msshop.order.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongUpdateInfoException extends RuntimeException {
    public WrongUpdateInfoException(String message) {
        super(message);
    }
}
