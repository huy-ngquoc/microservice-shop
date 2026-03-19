package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.out.persistence.mapper.ProductPersistenceMapper;
import vn.edu.uit.msshop.product.product.application.port.out.CreateProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.UpdateProductPort;
import vn.edu.uit.msshop.product.product.domain.model.NewProduct;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter
        implements LoadProductPort, CreateProductPort, UpdateProductPort {
    private final ProductMongoRepository repository;
    private final ProductPersistenceMapper mapper;

    @Override
    public Optional<Product> loadById(
            final ProductId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId).map(this.mapper::toDomain);
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
}
