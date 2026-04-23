package vn.uit.edu.msshop.order.adapter.in.web.request;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInventoryFromOrderServiceRequest {
    private String status;
    private String oldStatus;
    private List<OrderDetailRequest> detailRequests;
    private UUID orderId;
}
