package vn.uit.edu.msshop.account.application.port.out;



import vn.uit.edu.msshop.account.domain.event.kafka.AccountId;
import vn.uit.edu.msshop.account.domain.event.kafka.DeleteOldImageEvent;
import vn.uit.edu.msshop.account.domain.event.kafka.RollbackImageEvent;
import vn.uit.edu.msshop.account.domain.event.normal.AccountCreated;
import vn.uit.edu.msshop.account.domain.event.normal.AccountUpdate;

public interface PublishAccountEventPort {
    public void publish(AccountCreated event);
    public void publish(AccountUpdate event);
    public void sendAccountCreationFailEvent(AccountId accountId);
    public void sendDeleteOldImageEvent(DeleteOldImageEvent event);
    public void sendRollbackImageEvent(RollbackImageEvent event);
    
}
