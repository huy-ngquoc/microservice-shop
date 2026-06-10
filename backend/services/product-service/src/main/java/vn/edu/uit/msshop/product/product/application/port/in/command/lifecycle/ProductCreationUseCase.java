package vn.edu.uit.msshop.product.product.application.port.in.command.lifecycle;

import java.util.List;

import vn.edu.uit.msshop.product.product.application.dto.command.data.NewProductVariantData;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.lifecycle.ProductSimpleCreationCommand;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductCreationUseCase {
    ProductView create(
            final ProductCreationCommand cmd);

    default ProductView createSimple(
            final ProductSimpleCreationCommand cmd) {
        final var defaultVariant = new NewProductVariantData(
                cmd.productPrice(),
                List.of(),
                cmd.productTargetList());

        final var newCommand = new ProductCreationCommand(
                cmd.productName(),
                cmd.categoryId(),
                cmd.brandId(),
                List.of(),
                List.of(defaultVariant));

        return this.create(newCommand);
    }
}
