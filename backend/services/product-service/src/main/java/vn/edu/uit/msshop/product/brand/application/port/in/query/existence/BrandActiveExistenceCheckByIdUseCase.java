package vn.edu.uit.msshop.product.brand.application.port.in.query.existence;

import vn.edu.uit.msshop.product.brand.application.dto.query.existence.BrandActiveExistenceCheckByIdQuery;

public interface BrandActiveExistenceCheckByIdUseCase {
    boolean exists(
            final BrandActiveExistenceCheckByIdQuery query);
}
