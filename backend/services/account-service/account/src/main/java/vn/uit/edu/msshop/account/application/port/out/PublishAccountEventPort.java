package vn.uit.edu.msshop.account.application.port.out;

import vn.edu.uit.msshop.product.domain.model.account.event.AccountCreated;
import vn.edu.uit.msshop.product.domain.model.account.event.AccountUpdate;

public interface PublishAccountEventPort {
    public void publish(AccountCreated event);
    public void publish(AccountUpdate event);
}
