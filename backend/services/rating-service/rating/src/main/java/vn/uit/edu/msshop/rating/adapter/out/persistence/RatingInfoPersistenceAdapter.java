package vn.uit.edu.msshop.rating.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.adapter.out.persistence.mapper.RatingInfoEntityMapper;
import vn.uit.edu.msshop.rating.application.port.out.DeleteRatingInfoPort;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingInfoPort;
import vn.uit.edu.msshop.rating.application.port.out.SaveRatingInfoPort;
import vn.uit.edu.msshop.rating.domain.model.RatingInfo;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;

@Component
@RequiredArgsConstructor
public class RatingInfoPersistenceAdapter implements LoadRatingInfoPort, SaveRatingInfoPort, DeleteRatingInfoPort {
    private final RatingInfoEntityMapper mapper;
    private final RatingInfoMongoRepository repository;

    @Override
    public Optional<RatingInfo> loadById(ProductId productId) {
        final var result = repository.findById(productId.value());
        if(result.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDomain(result.get()));
    }

    @Override
    public List<RatingInfo> loadAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Page<RatingInfo> loadByPage(Pageable pageable) {
        final var result = repository.findAll(pageable);
        return result.map(mapper::toDomain);
    }

    @Override
    public RatingInfo save(RatingInfo ratingInfo) {
        final var result = repository.save(mapper.toDocument(ratingInfo));
        return mapper.toDomain(result);
    }

    @Override
    public void delete(ProductId productId) {
        repository.deleteById(productId.value());
    }

}
