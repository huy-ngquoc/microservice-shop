package vn.edu.uit.msshop.profile.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.profile.application.port.out.PublishProfileEventPort;
import vn.edu.uit.msshop.profile.domain.event.ProfileCreated;
import vn.edu.uit.msshop.profile.domain.event.ProfileUpdated;

@Component
@RequiredArgsConstructor
public class ProfileEventPublisherAdapter
        implements PublishProfileEventPort {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(
            final ProfileCreated event) {
        this.publisher.publishEvent(event);
    }

    @Override
    public void publish(
            final ProfileUpdated event) {
        this.publisher.publishEvent(event);
    }

}
