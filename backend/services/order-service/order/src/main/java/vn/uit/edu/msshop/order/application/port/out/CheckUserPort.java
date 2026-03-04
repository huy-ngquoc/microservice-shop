package vn.uit.edu.msshop.order.application.port.out;

import java.util.UUID;

public interface CheckUserPort {
    public boolean isUserAvailable(UUID userId);
}
