package vn.uit.edu.msshop.rating.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingInfoMongoRepository extends MongoRepository<RatingInfoDocument, UUID> {
    public Page<RatingInfoDocument> findAll(Pageable pageable);
    public Page<RatingInfoDocument> findByCreateAtBetweenOrUpdateAtBetween(Instant time1, Instant time2, Instant time3, Instant time4,Pageable pageable);
}
