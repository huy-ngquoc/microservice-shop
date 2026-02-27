package vn.uit.edu.msshop.account.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.domain.model.account.event.AccountCreated;
import vn.edu.uit.msshop.product.domain.model.account.event.AccountUpdate;
import vn.uit.edu.msshop.account.application.port.out.PublishAccountEventPort;
@Component
@RequiredArgsConstructor
public class AccountEventPublisherAdapter implements PublishAccountEventPort {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(AccountCreated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(AccountUpdate event) {
       this.publisher.publishEvent(event);
    }

}
