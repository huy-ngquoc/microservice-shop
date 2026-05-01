package vn.uit.edu.payment.application.port.out;

import vn.uit.edu.payment.domain.model.valueobject.UserId;

public interface CheckUserPort {
    public void checkUser(UserId userId);
}
