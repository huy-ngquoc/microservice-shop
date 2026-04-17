package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection="Orders")
public class OrderDocument {
    @Id
    private UUID id;

    private ShippingInfoDocument shippingInfo;

    private List<OrderDetailDocument> details;
    
    private String status;
    
    private UUID userId;

    private long originPrice;

    private long shippingFee;

    private long discount;

    private long totalPrice;

    private Instant createAt;

    private Instant updateAt;

    @Version
    private Long version;

    private String currency;
    private String paymentMethod;
    private String paymentStatus;


}
