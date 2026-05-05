package vn.edu.uit.msshop.product.brand.application.dto.command;

import org.jspecify.annotations.NullMarked;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;
import vn.edu.uit.msshop.shared.application.dto.Change;

@NullMarked
public record UpdateBrandInfoCommand(
        BrandId id,
        Change<BrandName> name,
        BrandVersion expectedVersion) {
}
