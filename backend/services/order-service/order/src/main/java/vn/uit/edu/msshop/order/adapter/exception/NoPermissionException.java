package vn.uit.edu.msshop.order.adapter.exception;

import java.util.UUID;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException(UUID userId) {
        super("User with id "+userId.toString()+" can not do this action");
    }
}
