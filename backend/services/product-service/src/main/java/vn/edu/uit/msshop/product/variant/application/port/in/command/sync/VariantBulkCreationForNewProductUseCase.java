package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import java.util.List;

import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkCreationForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;

public interface VariantBulkCreationForNewProductUseCase {
    List<VariantView> create(
            final VariantBulkCreationForNewProductCommand cmd);
}
