package vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command;

import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductStockCountBulkIncreationPort {
    void increaseAll(
            final Map<ProductId, Integer> incrementByProductId);
}
