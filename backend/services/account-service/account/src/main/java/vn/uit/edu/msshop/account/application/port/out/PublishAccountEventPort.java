package vn.uit.edu.msshop.account.application.port.out;



import vn.uit.edu.msshop.account.domain.event.kafka.AccountId;
import vn.uit.edu.msshop.account.domain.event.normal.AccountCreated;
import vn.uit.edu.msshop.account.domain.event.normal.AccountUpdate;

public interface PublishAccountEventPort {
    public void publish(AccountCreated event);
    public void publish(AccountUpdate event);
    public void sendAccountCreationFailEvent(AccountId accountId);
}
