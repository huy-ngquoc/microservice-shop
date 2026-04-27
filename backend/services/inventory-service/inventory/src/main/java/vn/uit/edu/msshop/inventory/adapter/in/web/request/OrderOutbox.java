package vn.uit.edu.msshop.inventory.adapter.in.web.request;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderOutbox {
    @Id
    private UUID id;
    private UUID orderId;
    private String type;
    private List<OrderDetailRequest> requests;
    private String orderStatus;
    private String outboxStatus;//PENDING, COMPLETED
    private Instant createdAt;
}
