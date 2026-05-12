package vn.uit.edu.msshop.order.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.edu.msshop.order.adapter.out.persistence.OrderDocument;
import vn.uit.edu.msshop.order.adapter.out.persistence.OrderOutbox;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCompositeEvent {
    private OrderDocument document;
    private OrderOutbox outbox;
}
