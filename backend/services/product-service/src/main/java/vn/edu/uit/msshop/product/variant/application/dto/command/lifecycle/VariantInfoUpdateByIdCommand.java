package vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle;

import vn.edu.uit.msshop.shared.application.dto.Change;

import java.util.List;
import java.util.UUID;

public record VariantInfoUpdateByIdCommand(
        UUID variantId,
        Change<Long> priceChange,
        Change<List<String>> traitListChange,
        Change<List<String>> targetListChange,
        long variantVersion) {

    public VariantInfoUpdateByIdCommand {
        traitListChange = traitListChange.map(List::copyOf);
        targetListChange = targetListChange.map(List::copyOf);
    }
}
