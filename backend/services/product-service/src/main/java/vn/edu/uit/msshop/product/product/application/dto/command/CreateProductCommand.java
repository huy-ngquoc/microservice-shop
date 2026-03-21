package vn.edu.uit.msshop.product.product.application.dto.command;

import java.util.List;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.ProductPrice;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record CreateProductCommand(
        ProductName name,

        ProductCategoryId categoryId,

        ProductBrandId brandId,

        ProductOptions options,

        List<CreateProductVariantCommand> variants) {
    public CreateProductCommand {
        CreateProductCommand.validateOptionsAndVariantsConsistency(
                options,
                variants);
    }

    private static void validateOptionsAndVariantsConsistency(
            final ProductOptions options,
            final List<CreateProductVariantCommand> variants) {
        if (options.isEmpty()) {
            if (variants.size() > 1) {
                throw new DomainException(
                        "Product with NO options CANNOT have more than 1 variant");
            }
            if (!variants.isEmpty() && !variants.getFirst().traits().isEmpty()) {
                throw new DomainException(
                        "Default variant must have empty traits");
            }
            return;
        }

        if (variants.isEmpty()) {
            throw new DomainException(
                    "Product with configurable options MUST have at least one variant");
        }

        final var expectedTierCount = options.size();
        for (final var variant : variants) {
            if (variant.traits().size() != expectedTierCount) {
                throw new DomainException(
                        String.format("Variant provides %d traits, but Product defines %d options",
                                variant.traits().size(), expectedTierCount));
            }
        }
    }
}
