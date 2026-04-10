package vn.edu.uit.msshop.product.product.application.port.in.command;

import java.util.List;

import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateSimpleProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;

public interface CreateProductUseCase {
    ProductView create(
            final CreateProductCommand command);

    default ProductView createSimple(
            final CreateSimpleProductCommand command) {
        final var defaultNewProductVariant = new NewProductVariant(
                command.price(),
                ProductVariantTraits.empty(),
                command.targets());

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
