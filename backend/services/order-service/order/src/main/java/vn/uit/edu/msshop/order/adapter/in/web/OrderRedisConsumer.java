package vn.uit.edu.msshop.order.adapter.in.web;


import java.time.Instant;
import java.util.UUID;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.OrderCreatedSuccessDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.inventory.OrderCreatedInventoryDocument;
import vn.uit.edu.msshop.order.adapter.out.event.documents.inventory.OrderDetailDocument;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedSuccessDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderCreatedInventoryDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.persistence.OrderDocument;
import vn.uit.edu.msshop.order.adapter.out.persistence.mapper.OrderDataMapper;
import vn.uit.edu.msshop.order.application.port.out.DeleteOrderPort;
import vn.uit.edu.msshop.order.application.port.out.PublishOrderEventPort;
import vn.uit.edu.msshop.order.application.port.out.SaveOrderPort;
import vn.uit.edu.msshop.order.domain.model.Order;
@Service
@RequiredArgsConstructor
public class OrderRedisConsumer implements StreamListener<String, MapRecord<String,String,String>> {
    private final SaveOrderPort savePort;
    private final RedisTemplate redisTemplate;
    private final DeleteOrderPort deletePort;
    private final OrderCreatedInventoryDocumentRepository orderCreatedInventoryDocumentRepository;
    private final PublishOrderEventPort publishPort;
    private final OrderCreatedDocumentRepository orderCreatedDocumentRepo;
    private final OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo;
    private final OrderDataMapper mapper;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        // Quan trọng: Để Instant trả về dạng chuỗi ISO thay vì mảng số
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    @Override
    @Transactional
    public void onMessage(MapRecord<String, String, String> message) {
    
        
        String jsonPayload = message.getValue().get("payload");
        Order order=null;
        try {
        // 2. Giải mã toàn bộ Object (xử lý ngon lành nested object và List)
        OrderDocument document = objectMapper.readValue(jsonPayload, OrderDocument.class);
        // 3. Dùng Jackson để convert từ Map sang OrderDocument "xịn"
        order = mapper.toDomain(document);
        System.out.println("On order created");
        
        final Order saved =savePort.save(order);
        OrderCreatedInventoryDocument outboxEventOrderCreatedInventory= OrderCreatedInventoryDocument.builder().eventId(UUID.randomUUID())
        .orderId(saved.getId().value()).eventStatus("PENDING")
        .orderDetails(saved.getDetails().stream().map(item->new OrderDetailDocument(item.variantId(), item.amount())).toList())
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null)
        .build();
        final var savedOutboxEventOrderCreatedInventory = orderCreatedInventoryDocumentRepository.save(outboxEventOrderCreatedInventory);
        OrderCreatedDocument outboxEventOrderCreated = OrderCreatedDocument.builder().currency(saved.getCurrency().value())
        .orderId(saved.getId().value())
        .paymentMethod(saved.getPaymentMethod().value())
        .paymentValue(saved.getTotalPrice().value())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null).eventId(UUID.randomUUID())
        .lastError(null).userId(saved.getUserId().value()).build();
        final var savedEvent=orderCreatedDocumentRepo.save(outboxEventOrderCreated);
         OrderCreatedSuccessDocument outboxOrderCreatedSuccess = OrderCreatedSuccessDocument.builder().eventId(UUID.randomUUID())
        .userId(order.getUserId().value())
        .variantIds(order.getDetails().stream().map(item->item.variantId()).toList())
        .eventStatus("PENDING")
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null).build();
        final var savedOutboxOrderCreatedSuccess = orderCreatedSuccessDocumentRepo.save(outboxOrderCreatedSuccess);
        
        
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishPort.publishOrderCreated_InventoryEvent(savedOutboxEventOrderCreatedInventory);
                publishPort.publishOrderCreatedEvent(savedEvent);
                publishPort.publishClearCartEvent(savedOutboxOrderCreatedSuccess);
                redisTemplate.opsForStream().acknowledge("order_stream", "order-group", message.getId());
                redisTemplate.opsForStream().delete("order_stream", message.getId());
            }
        });
        }
        catch(Exception e) {
            e.printStackTrace();
            if(order==null) return;
            deletePort.deleteById(order.getId());
        }

    }


}
