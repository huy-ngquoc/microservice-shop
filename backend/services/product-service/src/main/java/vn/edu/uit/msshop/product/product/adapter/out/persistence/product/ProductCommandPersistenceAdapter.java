package vn.edu.uit.msshop.product.product.adapter.out.persistence.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductCreationPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductDeletionByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductBulkUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.command.ProductUpdatePort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProduct;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Component
@RequiredArgsConstructor
class ProductCommandPersistenceAdapter
        implements
        ProductCreationPort,
        ProductUpdatePort,
        ProductBulkUpdatePort,
        ProductDeletionByIdPort {
    private final ProductMongoRepository repository;
    private final ProductPersistenceMapper mapper;

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

        final ProductDocument saved;
        try {
            saved = this.repository.save(toSave);
        } catch (final OptimisticLockingFailureException _) {
            final var expected = product.getVersion().value();
            final var current = this.repository.findById(toSave.getId())
                    .map(ProductDocument::getVersion)
                    .orElse(-1L);
            throw new OptimisticLockException(expected, current);
        }

        return this.mapper.toDomain(saved);
    }

    @Override
    public List<Product> updateAll(
            final Collection<Product> products) {
        if (products.isEmpty()) {
            return List.of();
        }

        final var toSaveAll = products.stream()
                .map(this.mapper::toPersistence)
                .toList();
        final var savedAll = new ArrayList<ProductDocument>(toSaveAll.size());
        for (final var toSave : toSaveAll) {
            final ProductDocument saved;
            try {
                saved = this.repository.save(toSave);
            } catch (final OptimisticLockingFailureException _) {
                final var expected = toSave.getVersion();
                final var current = this.repository.findById(toSave.getId())
                        .map(ProductDocument::getVersion)
                        .orElse(null);
                throw new OptimisticLockException(expected, current);
            }

            savedAll.add(saved);
        }

        return savedAll.stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(
            final ProductId id) {
        final var jpaId = id.value();
        this.repository.deleteById(jpaId);
    }
}
