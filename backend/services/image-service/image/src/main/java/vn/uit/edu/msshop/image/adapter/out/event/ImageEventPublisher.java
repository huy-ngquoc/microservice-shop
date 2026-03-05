package vn.uit.edu.msshop.image.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.application.port.out.PublishImageEventPort;
import vn.uit.edu.msshop.image.domain.event.ImageDeleted;
import vn.uit.edu.msshop.image.domain.event.ImageUpdated;

@Component
@RequiredArgsConstructor
public class ImageEventPublisher implements PublishImageEventPort {
     private final ApplicationEventPublisher publisher;

    @Override
    public void publish(ImageUpdated imageUpdated) {
        publisher.publishEvent(imageUpdated);
    }

    @Override
    public void publish(ImageDeleted imageDeleted) {
        publisher.publishEvent(imageDeleted);
    }

}
