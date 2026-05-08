package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderOutboxRepository extends MongoRepository<OrderOutbox, UUID>{
    public List<OrderOutbox> findTop50ByOutboxStatusOrderByCreatedAtAsc(String outboxStatus);
}
