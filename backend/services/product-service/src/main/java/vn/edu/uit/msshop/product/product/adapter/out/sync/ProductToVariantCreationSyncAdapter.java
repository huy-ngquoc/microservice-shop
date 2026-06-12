package vn.edu.uit.msshop.product.product.adapter.out.sync;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.out.sync.mapper.ProductToVariantCreationSyncMapper;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkCreationPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.variant.application.port.in.command.sync.VariantBulkCreationForNewProductUseCase;

@Component
@RequiredArgsConstructor
public class ProductToVariantCreationSyncAdapter
        implements ProductVariantBulkCreationPort {

    private final VariantBulkCreationForNewProductUseCase variantBulkCreationForNewProductUseCase;
    private final ProductToVariantCreationSyncMapper mapper;

    @Override
    public ProductVariants create(
            final ProductId id,
            final ProductName name,
            final NewProductVariants newVariants) {
        final var command = this.mapper.toCreateCommand(
                id,
                name,
                newVariants);
        final var variantViewsList = this.variantBulkCreationForNewProductUseCase.create(command);

        return this.mapper.toProductVariants(variantViewsList);
    }

}
