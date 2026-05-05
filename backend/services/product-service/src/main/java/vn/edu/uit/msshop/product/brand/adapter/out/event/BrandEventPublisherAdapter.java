package vn.edu.uit.msshop.product.brand.adapter.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandCreated;
import vn.edu.uit.msshop.product.brand.domain.event.BrandLogoUpdated;
import vn.edu.uit.msshop.product.brand.domain.event.BrandPurged;
import vn.edu.uit.msshop.product.brand.domain.event.BrandRestored;
import vn.edu.uit.msshop.product.brand.domain.event.BrandSoftDeleted;
import vn.edu.uit.msshop.product.brand.domain.event.BrandUpdated;

@Component
@RequiredArgsConstructor
public class BrandEventPublisherAdapter implements PublishBrandEventPort {
  private final ApplicationEventPublisher publisher;

  @Override
  public void publish(final BrandCreated event) {
    this.publisher.publishEvent(event);
  }

  @Override
  public void publish(final BrandUpdated event) {
    this.publisher.publishEvent(event);
  }

  @Override
  public void publish(final BrandLogoUpdated event) {
    this.publisher.publishEvent(event);
  }

  @Override
  public void publish(final BrandSoftDeleted event) {
    this.publisher.publishEvent(event);
  }

  @Override
  public void publish(final BrandRestored event) {
    this.publisher.publishEvent(event);
  }

  @Override
  public void publish(final BrandPurged event) {
    this.publisher.publishEvent(event);
  }
}
