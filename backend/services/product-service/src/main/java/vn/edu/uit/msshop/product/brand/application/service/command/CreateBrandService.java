package vn.edu.uit.msshop.product.brand.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.brand.application.dto.command.CreateBrandCommand;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.command.CreateBrandUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.event.PublishBrandEventPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.CreateBrandPort;
import vn.edu.uit.msshop.product.brand.domain.event.BrandCreated;
import vn.edu.uit.msshop.product.brand.domain.model.creation.NewBrand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateBrandService implements CreateBrandUseCase {
  private final CreateBrandPort createPort;
  private final BrandViewMapper mapper;
  private final PublishBrandEventPort eventPort;

  @Override
  @Transactional
  public BrandView create(final CreateBrandCommand command) {
    final var brand = new NewBrand(BrandId.newId(), command.name());

    final var saved = this.createPort.create(brand);
    this.eventPort.publish(new BrandCreated(saved.getId()));

    return this.mapper.toView(saved);
  }
}
