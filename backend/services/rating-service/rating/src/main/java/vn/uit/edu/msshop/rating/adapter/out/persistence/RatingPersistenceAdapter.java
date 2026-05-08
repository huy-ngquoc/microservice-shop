package vn.uit.edu.msshop.rating.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.adapter.out.persistence.mapper.RatingEntityMapper;
import vn.uit.edu.msshop.rating.application.exception.RatingNotFoundException;
import vn.uit.edu.msshop.rating.application.port.out.DeleteRatingPort;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingPort;
import vn.uit.edu.msshop.rating.application.port.out.SaveRatingPort;
import vn.uit.edu.msshop.rating.domain.model.Rating;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;
@Component
@RequiredArgsConstructor
public class RatingPersistenceAdapter implements LoadRatingPort,SaveRatingPort,DeleteRatingPort {
    private final RatingMongoRepository repository;
    private final RatingEntityMapper mapper;

    @Override
    public Rating loadById(RatingId id) {
        return mapper.toDomain(repository.findById(id.value()).orElseThrow(()->new RatingNotFoundException(id)));
    }

    @Override
    public Rating save(Rating rating) {
        return mapper.toDomain(repository.save(mapper.toEntity(rating)));
    }

    @Override
    public void deleteById(RatingId id) {
        repository.deleteById(id.value());
    }

    @Override
    public Page<Rating> loadByProduct(ProductId id, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        return repository.findByProductId(id.value(), pageable).map(mapper::toDomain);
    }

}
