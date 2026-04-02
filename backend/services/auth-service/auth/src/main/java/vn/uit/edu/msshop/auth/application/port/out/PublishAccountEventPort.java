package vn.uit.edu.msshop.auth.application.port.out;

import vn.uit.edu.msshop.auth.adapter.out.event.documents.AccountCreatedDocument;

public interface PublishAccountEventPort {
    public void sendAccountCreateEvent(AccountCreatedDocument outboxEvent);
}
