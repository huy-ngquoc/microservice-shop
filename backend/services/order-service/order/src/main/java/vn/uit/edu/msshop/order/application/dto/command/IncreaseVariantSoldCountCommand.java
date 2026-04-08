package vn.uit.edu.msshop.order.application.dto.command;

import vn.uit.edu.msshop.order.domain.model.valueobject.Amount;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;

public record IncreaseVariantSoldCountCommand(VariantId id, Amount amount) {

}
