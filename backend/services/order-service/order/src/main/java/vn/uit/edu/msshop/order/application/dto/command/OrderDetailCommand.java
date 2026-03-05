package vn.uit.edu.msshop.order.application.dto.command;
import java.util.UUID;

public record OrderDetailCommand(UUID variantId, int quantity) {

}
