package vn.uit.edu.msshop.order.application.dto.command;

import vn.uit.edu.msshop.order.domain.model.valueobject.SoldCount;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;

public record CreateVariantSoldCountCommand(VariantId id, SoldCount soldCount) {

}
