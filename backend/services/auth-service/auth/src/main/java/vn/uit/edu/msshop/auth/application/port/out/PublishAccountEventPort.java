package vn.uit.edu.msshop.auth.application.port.out;

import vn.uit.edu.msshop.auth.domain.event.AccountCreated;

public interface PublishAccountEventPort {
    public void sendAccountCreateEvent(AccountCreated accountCreated);
}
