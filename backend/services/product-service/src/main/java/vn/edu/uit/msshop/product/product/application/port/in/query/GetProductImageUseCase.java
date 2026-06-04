package vn.edu.uit.msshop.product.product.application.port.in.query;

import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface GetProductImageUseCase {
    public List<String> getImageURLFromId(ProductId id);
}
