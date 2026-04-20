package vn.uit.edu.msshop.order.domain.event;

import java.util.UUID;

public class OrderReceived {
    private UUID eventId;
    private UUID orderId;
    private String userEmail;
}
