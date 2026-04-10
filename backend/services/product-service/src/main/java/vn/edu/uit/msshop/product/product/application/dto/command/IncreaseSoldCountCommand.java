package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.IncreaseAmount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record IncreaseSoldCountCommand(ProductId id, IncreaseAmount increaseAmount) {

}
