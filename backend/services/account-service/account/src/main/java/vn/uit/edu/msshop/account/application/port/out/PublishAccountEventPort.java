package vn.uit.edu.msshop.account.application.port.out;



import vn.uit.edu.msshop.account.adapter.out.event.AccountCreatedDocument;
import vn.uit.edu.msshop.account.adapter.out.event.AccountIdDocument;
import vn.uit.edu.msshop.account.adapter.out.event.DeleteOldImageEventDocument;
import vn.uit.edu.msshop.account.adapter.out.event.RollbackImageEventDocument;
import vn.uit.edu.msshop.account.domain.event.normal.AccountCreated;
import vn.uit.edu.msshop.account.domain.event.normal.AccountUpdate;

public interface PublishAccountEventPort {
    public void publish(AccountCreated event);
    public void publish(AccountUpdate event);
    public void sendAccountCreationFailEvent(AccountIdDocument outboxEvent);
    public void sendDeleteOldImageEvent(DeleteOldImageEventDocument outboxEvent);
    public void sendRollbackImageEvent(RollbackImageEventDocument outboxEvent);
    public void sendAccountCreated(AccountCreatedDocument outboxEvent);
    
}
