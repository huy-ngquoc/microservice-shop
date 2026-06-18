package vn.edu.uit.msshop.product.product.application.port.in.query.existence;

import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductExistenceCheckByVariantIdQuery;

/**
 * Checks whether ANY product still references the given variant in its embedded variant projection,
 * regardless of the product's deletion state.
 *
 * <p>This is intentionally NOT limited to active products: a soft-deleted product keeps its variant
 * projection and may be restored, so a variant it references must not be hard-deleted. Used to guard
 * variant purge.
 */
public interface ProductExistenceCheckByVariantIdUseCase {
    boolean exists(
            final ProductExistenceCheckByVariantIdQuery query);
}
