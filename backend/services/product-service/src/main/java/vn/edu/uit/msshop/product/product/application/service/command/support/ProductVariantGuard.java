package vn.edu.uit.msshop.product.product.application.service.command.support;

import java.util.Collection;

import vn.edu.uit.msshop.product.product.application.exception.ProductMustHaveAtLeastOneVariantException;
import vn.edu.uit.msshop.product.product.application.exception.ProductSimpleCannotAddVariantException;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

public final class ProductVariantGuard {

    private ProductVariantGuard() {
    }

    public static void ensureNotProductSimple(
            final Product product) {
        if (product.getOptions().isEmpty()) {
            throw new ProductSimpleCannotAddVariantException(product.getId());
        }
    }

    public static void ensureNoDuplicateCombination(
            final Product product,
            final ProductVariantTraits traits) {
        if (product.getVariants().combinationExists(traits)) {
            final var msg = String.format(
                    "A variant with the same trait combination already exists: %s",
                    traits.unwrap());
            throw new BusinessRuleException(msg);
        }
    }

    public static void ensureAllVariantsExist(
            final Product product,
            final Collection<ProductVariantId> ids) {
        if (!product.getVariants().containsAllIds(ids)) {
            final var msg = String.format(
                    "One or more variants do not belong to product '%s'",
                    product.getId().value());
            throw new BusinessRuleException(msg);
        }
    }

    public static void ensureAtLeastOneVariantRemains(
            final Product product,
            final Collection<ProductVariantId> idCollectionToRemove) {
        if (product.getVariants().removingByIdsLeavesEmpty(idCollectionToRemove)) {
            throw new ProductMustHaveAtLeastOneVariantException(product.getId());
        }
    }
}
