package vn.uit.edu.msshop.order.adapter.out.persistence;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.persistence.mapper.OrderDataMapper;
import vn.uit.edu.msshop.order.application.port.out.DeleteOrderPort;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.application.port.out.SaveRedisStreamPort;
import vn.uit.edu.msshop.order.domain.model.Order;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderStatus;
import vn.uit.edu.msshop.order.domain.model.valueobject.UserId;
@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements LoadOrderPort,SaveOrderPort, DeleteOrderPort, SaveRedisStreamPort {

    private final OrderRepository orderRepo;
    private final OrderDataMapper orderDataMapper;
    private final MongoTemplate mongoTemplate;
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    @PostConstruct
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        // Quan trọng: Để Instant trả về dạng chuỗi ISO thay vì mảng số
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    @Override
    public Optional<Order> loadById(OrderId orderId) {
        Optional<OrderDocument> document = orderRepo.findById(orderId.value());
        return document.map(orderDataMapper::toDomain);
    }

    @Override
    public Page<Order> filterOrder(Optional<UUID> variantId, Optional<OrderStatus> status, Optional<Integer> minPrice, Optional<Integer> maxPrice, Optional<UserId> userId, Optional<Instant> createFrom, Optional<Instant> createTo, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Query query = new Query().with(pageable);
        List<Criteria> criteriaList = new ArrayList<>();

        
        variantId.ifPresent(id -> 
            criteriaList.add(Criteria.where("details.variantId").is(id)));

        
        status.ifPresent(s -> 
            criteriaList.add(Criteria.where("status").is(s.value())));

        
        userId.ifPresent(id -> 
            criteriaList.add(Criteria.where("userId").is(id.value())));

        
        if (minPrice.isPresent() || maxPrice.isPresent()) {
            Criteria priceCriteria = Criteria.where("totalPrice");
            minPrice.ifPresent(priceCriteria::gte);
            maxPrice.ifPresent(priceCriteria::lte);
            criteriaList.add(priceCriteria);
        }

        
        if (createFrom.isPresent() || createTo.isPresent()) {
            Criteria dateCriteria = Criteria.where("createAt");
            createFrom.ifPresent(dateCriteria::gte);
            createTo.ifPresent(dateCriteria::lte);
            criteriaList.add(dateCriteria);
        }

        
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        
        
        List<OrderDocument> entities = mongoTemplate.find(query, OrderDocument.class);
        
        
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), OrderDocument.class);

        
        List<Order> orders = entities.stream()
                .map(orderDataMapper::toDomain)
                .toList();

        return PageableExecutionUtils.getPage(orders, pageable, () -> total);

    }

    @Override
    @Observed(name = "mongodb.save.order")
    public Order save(Order order) {
        OrderDocument orderDocument = this.orderDataMapper.toDocument(order);
        orderRepo.save(orderDocument);
        return order;
    }

    @Override
    public void deleteById(OrderId orderId) {
        orderRepo.deleteById(orderId.value());
    }

    @Override
    public void deleteAll() {
        orderRepo.deleteAll();
    }

    @Override
    public List<Order> loadAll() {
        return orderRepo.findAll().stream().map(orderDataMapper::toDomain).toList();
    }

    @Override
    public void saveToStream(Order order) {
        final OrderDocument document = orderDataMapper.toDocument(order);
       try {
        // 1. Biến cả cái OrderDocument (bao gồm list, object lồng) thành 1 chuỗi JSON duy nhất
        String jsonPayload = objectMapper.writeValueAsString(document);

        // 2. Lưu vào Redis dưới dạng một Map đơn giản có 1 key là "payload"
        Map<String, String> map = Map.of("payload", jsonPayload);

        MapRecord<String, String, String> record = StreamRecords.newRecord()
                .in("order_stream")
                .ofMap(map);

        redisTemplate.opsForStream().add(record);
    } catch (JsonProcessingException e) {
        throw new RuntimeException("Lỗi đóng gói JSON", e);
    }

    }


}
