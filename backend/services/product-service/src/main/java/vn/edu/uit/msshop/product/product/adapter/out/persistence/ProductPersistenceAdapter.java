package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.out.persistence.mapper.ProductPersistenceMapper;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CheckProductExistsByBrandPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CheckProductExistsByCategoryPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CheckProductExistsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CreateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ListProductsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadSoftDeletedProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProduct;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.shared.application.dto.response.PageResponseDto;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter
        implements
        ListProductsPort,
        LoadProductPort,
        LoadSoftDeletedProductPort,
        CheckProductExistsPort,
        CheckProductExistsByBrandPort,
        CheckProductExistsByCategoryPort,
        CreateProductPort,
        UpdateProductPort {
    private final ProductMongoRepository repository;
    private final ProductPersistenceMapper mapper;

    @Override
    public PageResponseDto<Product> list(
            final PageRequestDto pageRequest) {
        final var pageable = ProductPersistenceAdapter.toPageable(
                pageRequest,
                ProductDocument.Fields.id);
        final var page = this.repository.findAllByDeletionTimeIsNull(pageable);

        final var products = page.getContent().stream()
                .map(this.mapper::toDomain)
                .toList();

        return new PageResponseDto<>(
                products,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public Optional<Product> loadById(
            final ProductId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Optional<Product> loadSoftDeletedById(
            final ProductId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNotNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public boolean existsById(
            final ProductId id) {
        final var jpaId = id.value();
        return this.repository.existsById(jpaId);
    }

    @Override
    public boolean existsByBrandId(
            final ProductBrandId brandId) {
        final var jpaBrandId = brandId.value();
        return this.repository.existsByBrandId(jpaBrandId);
    }

    @Override
    public boolean existsByCategoryId(
            final ProductCategoryId categoryId) {
        final var jpaCategoryId = categoryId.value();
        return this.repository.existsByCategoryId(jpaCategoryId);
    }

    @Override
    public Product create(
            final NewProduct newProduct) {
        final var toSave = this.mapper.toPersistence(newProduct);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }

    @Override
    public Product update(
            final Product product) {
        final var toSave = this.mapper.toPersistence(product);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }

    private static PageRequest toPageable(
            final PageRequestDto request,
            final String defaultSortField) {
        final var direction = switch (request.direction()) {
            case ASC -> Sort.Direction.ASC;
            case DESC -> Sort.Direction.DESC;
        };

        final var sortBy = request.sortBy();
        final var sortField = (sortBy != null) ? sortBy : defaultSortField;
        final var sort = Sort.by(direction, sortField);

        return PageRequest.of(request.page(), request.size(), sort);
    }
}
