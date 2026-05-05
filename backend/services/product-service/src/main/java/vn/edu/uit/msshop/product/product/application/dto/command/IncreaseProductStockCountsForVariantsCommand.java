package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public record IncreaseProductStockCountsForVariantsCommand(Map<ProductId,Integer>incrementById){public IncreaseProductStockCountsForVariantsCommand{incrementById=Map.copyOf(incrementById);}}
