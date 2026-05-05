package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record DecreaseProductStockCountsForVariantsCommand(Map<ProductId,Integer>decrementById){public DecreaseProductStockCountsForVariantsCommand{decrementById=Map.copyOf(decrementById);}}
