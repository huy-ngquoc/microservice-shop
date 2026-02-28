package vn.uit.edu.msshop.account.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.account.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.account.domain.event.AccountCreated;
import vn.uit.edu.msshop.account.domain.event.AccountUpdate;
@Component

public class AccountEventPublisherAdapter implements PublishAccountEventPort {
    private final ApplicationEventPublisher publisher;
    public AccountEventPublisherAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(AccountCreated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(AccountUpdate event) {
       this.publisher.publishEvent(event);
    }

}
