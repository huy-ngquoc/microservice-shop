package vn.uit.edu.msshop.account.application.port.out;



import vn.uit.edu.msshop.account.domain.event.AccountCreated;
import vn.uit.edu.msshop.account.domain.event.AccountUpdate;

public interface PublishAccountEventPort {
    public void publish(AccountCreated event);
    public void publish(AccountUpdate event);
}
