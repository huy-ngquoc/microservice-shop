package vn.uit.edu.msshop.rating.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingInfoMongoRepository extends MongoRepository<RatingInfoDocument, UUID> {
    public Page<RatingInfoDocument> findAll(Pageable pageable);
}
