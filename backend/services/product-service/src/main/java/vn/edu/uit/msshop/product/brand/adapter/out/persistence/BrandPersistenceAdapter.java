package vn.edu.uit.msshop.product.brand.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.adapter.out.persistence.mapper.BrandPersistenceMapper;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.CheckBrandExistsPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.CreateBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.DeleteBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.ListBrandsPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.ListSoftDeletedBrandsPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadSoftDeletedBrandPort;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.UpdateBrandPort;
import vn.edu.uit.msshop.product.brand.domain.model.Brand;
import vn.edu.uit.msshop.product.brand.domain.model.creation.NewBrand;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.shared.adapter.out.persistence.PageRequests;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Component
@RequiredArgsConstructor
public class BrandPersistenceAdapter
    implements ListBrandsPort, ListSoftDeletedBrandsPort, LoadBrandPort, LoadSoftDeletedBrandPort,
    CheckBrandExistsPort, CreateBrandPort, UpdateBrandPort, DeleteBrandPort {
  private final BrandMongoRepository repository;
  private final BrandPersistenceMapper mapper;

  @Override
  public PageResponseDto<Brand> list(final PageRequestDto pageRequest) {
    final var pageable = PageRequests.toPageable(pageRequest, BrandDocument.Fields.id);
    final var page = this.repository.findAllByDeletionTimeIsNull(pageable);

    final var brands = page.getContent().stream().map(this.mapper::toDomain).toList();

    return new PageResponseDto<>(brands, page.getNumber(), page.getSize(), page.getTotalElements());
  }

  @Override
  public PageResponseDto<Brand> listSoftDeleted(final PageRequestDto pageRequest) {
    final var pageable = PageRequests.toPageable(pageRequest, BrandDocument.Fields.id);
    final var page = this.repository.findAllByDeletionTimeIsNotNull(pageable);

    final var brands = page.getContent().stream().map(this.mapper::toDomain).toList();

    return new PageResponseDto<>(brands, page.getNumber(), page.getSize(), page.getTotalElements());
  }

  @Override
  public Optional<Brand> loadById(final BrandId id) {
    final var jpaId = id.value();
    return this.repository.findByIdAndDeletionTimeIsNull(jpaId).map(this.mapper::toDomain);
  }

  @Override
  public Optional<Brand> loadSoftDeletedById(final BrandId id) {
    final var jpaId = id.value();
    return this.repository.findByIdAndDeletionTimeIsNotNull(jpaId).map(this.mapper::toDomain);
  }

  @Override
  public boolean existsById(final BrandId id) {
    final var jpaId = id.value();
    return this.repository.existsByIdAndDeletionTimeIsNull(jpaId);
  }

  @Override
  public Brand create(final NewBrand newBrand) {
    final var toSave = this.mapper.toPersistence(newBrand);
    final var saved = this.repository.save(toSave);
    return this.mapper.toDomain(saved);
  }

  @Override
  public Brand update(final Brand brand) {
    final var toSave = this.mapper.toPersistence(brand);
    final var saved = this.repository.save(toSave);
    return this.mapper.toDomain(saved);
  }

  @Override
  public void deleteById(final BrandId id) {
    final var jpaId = id.value();
    this.repository.deleteById(jpaId);
  }
}
