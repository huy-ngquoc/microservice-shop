package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.out.persistence.mapper.ProductRatingPersistenceMapper;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.DeleteProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.InitializeProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadAllProductRatingsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductRatingPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Component
@RequiredArgsConstructor
public class ProductRatingPersistenceAdapter
        implements
        LoadProductRatingPort,
        LoadAllProductRatingsPort,
        InitializeProductRatingPort,
        UpdateProductRatingPort,
        DeleteProductRatingPort {
    private static final Collector<ProductRating, ?, Map<ProductId, ProductRating>> COLLECTOR = Collectors
            .toUnmodifiableMap(
                    ProductRating::getId,
                    Function.identity(),
                    (
                            existing,
                            replacement) -> existing);

    private final ProductRatingMongoRepository repository;
    private final ProductRatingPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<ProductRating> loadById(
            final ProductId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Map<ProductId, ProductRating> loadAllByIds(
            final Set<ProductId> ids) {
        final var jpaIds = ids.stream()
                .map(ProductId::value)
                .toList();
        return this.repository.findAllById(jpaIds).stream()
                .map(this.mapper::toDomain)
                .collect(ProductRatingPersistenceAdapter.COLLECTOR);
    }

    @Override
    public ProductRating initialize(
            final ProductId id) {
        final var jpaId = id.value();

        final var query = new Query(Criteria.where("_id").is(jpaId));
        final var update = new Update()
                .setOnInsert(ProductRatingDocument.Fields.average, 0.0F)
                .setOnInsert(ProductRatingDocument.Fields.amount, 0)
                .setOnInsert(ProductRatingDocument.Fields.lastUpdatedTime, Instant.now());

        return this.upsertAndReturnDomain(query, update);
    }

    @Override
    public ProductRating update(
            final ProductRating rating) {
        final var query = new Query(Criteria.where("_id").is(rating.getId().value()));
        final var update = new Update()
                .set(ProductRatingDocument.Fields.average, rating.getAverage().value())
                .set(ProductRatingDocument.Fields.amount, rating.getAmount().value())
                .set(ProductRatingDocument.Fields.lastUpdatedTime, Instant.now());

        return this.upsertAndReturnDomain(query, update);
    }

    @Override
    public void deleteById(
            final ProductId id) {
        final var jpaId = id.value();
        this.repository.deleteById(jpaId);
    }

    private ProductRating upsertAndReturnDomain(
            final Query query,
            final Update update) {
        final var options = FindAndModifyOptions
                .options()
                .returnNew(true)
                .upsert(true);
        final var doc = this.mongoTemplate.findAndModify(
                query,
                update,
                options,
                ProductRatingDocument.class);
        Objects.requireNonNull(doc, "find-and-modify with upsert must return a non-null document");

        return this.mapper.toDomain(doc);
    }
}
