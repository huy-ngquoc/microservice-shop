package vn.uit.edu.msshop.account.application.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(
            String message) {
        super(message);
    }
}
