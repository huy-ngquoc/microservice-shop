package vn.edu.uit.msshop.product.product.application.port.in;

import java.util.List;

import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateSimpleProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.domain.model.NewProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantTraits;

public interface CreateProductUseCase {
    ProductView create(
            final CreateProductCommand command);

    default ProductView createSimple(
            final CreateSimpleProductCommand command) {
        final var defaultNewProductVariant = new NewProductVariant(
                new ProductVariantPrice(command.price().value()),
                ProductVariantTraits.empty());

        final var defaultNewProductVariants = new NewProductVariants(
                List.of(defaultNewProductVariant));

        final var defaultNewProductConfiguration = new NewProductConfiguration(
                ProductOptions.empty(),
                defaultNewProductVariants);

        final var newCommand = new CreateProductCommand(
                command.name(),
                command.categoryId(),
                command.brandId(),
                defaultNewProductConfiguration);

        return this.create(newCommand);
    }
}
