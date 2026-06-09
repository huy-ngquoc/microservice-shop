package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import java.util.List;

import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSimpleCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;

public interface ProductCreationUseCase {
    ProductView create(
            final ProductCreationCommand command);

    default ProductView createSimple(
            final ProductSimpleCreationCommand command) {
        final var defaultNewProductVariant = new NewProductVariant(
                command.price(),
                ProductVariantTraits.empty(),
                command.targets());

        final var defaultNewProductVariants = new NewProductVariants(
                List.of(defaultNewProductVariant));

        final var defaultNewProductConfiguration = new NewProductConfiguration(
                ProductOptions.empty(),
                defaultNewProductVariants);

        final var newCommand = new ProductCreationCommand(
                command.name(),
                command.categoryId(),
                command.brandId(),
                defaultNewProductConfiguration);

        return this.create(newCommand);
    }
}
