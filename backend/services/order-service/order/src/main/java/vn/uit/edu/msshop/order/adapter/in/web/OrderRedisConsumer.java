package vn.uit.edu.msshop.order.adapter.in.web;


import java.util.List;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.in.web.request.OrderDetailRequest;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedSuccessDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderCreatedInventoryDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.persistence.OrderDocument;
import vn.uit.edu.msshop.order.adapter.out.persistence.mapper.OrderDataMapper;
import vn.uit.edu.msshop.order.adapter.remote.InventoryChecker;
import vn.uit.edu.msshop.order.application.port.in.CreateOrderUseCase;
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
    private final InventoryChecker inventoryChecker;
    private final CreateOrderUseCase createUseCase;

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
        
        
        final Order saved =savePort.save(order);
        /*OrderCreatedInventoryDocument outboxEventOrderCreatedInventory= OrderCreatedInventoryDocument.builder().eventId(UUID.randomUUID())
        .orderId(saved.getId().value()).eventStatus("PENDING")
        .orderDetails(saved.getDetails().stream().map(item->new OrderDetailDocument(item.variantId(), item.amount())).toList())
        .retryCount(0)
        .createdAt(Instant.now())
        .updatedAt(null)
        .lastError(null)
        .build();
        final var savedOutboxEventOrderCreatedInventory = orderCreatedInventoryDocumentRepository.save(outboxEventOrderCreatedInventory);*/
        createUseCase.manualCreate(saved);
        redisTemplate.opsForStream().acknowledge("order_stream", "order-group", message.getId());
        redisTemplate.opsForStream().delete("order_stream", message.getId());
        }
        catch(Exception e) {
            e.printStackTrace();
            if(order==null) return;
            deletePort.deleteById(order.getId());
            redisTemplate.opsForStream().acknowledge("order_stream", "order-group", message.getId());
        redisTemplate.opsForStream().delete("order_stream", message.getId());
            List<OrderDetailRequest> requests = order.getDetails().stream().map(item->new OrderDetailRequest(item.variantId(), item.amount())).toList();
            inventoryChecker.rollback(requests, "CREATE_ORDER_FAILED");

        }

    }


}
