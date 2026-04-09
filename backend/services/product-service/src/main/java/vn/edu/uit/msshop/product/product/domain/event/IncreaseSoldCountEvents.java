package vn.edu.uit.msshop.product.product.domain.event;

import java.util.List;
import java.util.UUID;

public record IncreaseSoldCountEvents(UUID eventId, List<IncreaseSoldCountDetail> details) {

}
