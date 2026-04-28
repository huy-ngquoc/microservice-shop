package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderDocument, UUID>{
    public List<OrderDocument> findTop50ByStatusAndPaymentMethodAndUpdateAtBefore(String status,String paymentMethod,Instant threshold);
}
