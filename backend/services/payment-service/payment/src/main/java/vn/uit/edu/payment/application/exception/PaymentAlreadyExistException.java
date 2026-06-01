package vn.uit.edu.payment.application.exception;

public class PaymentAlreadyExistException extends RuntimeException {
    public PaymentAlreadyExistException(
            String message) {
        super(message);
    }
}
