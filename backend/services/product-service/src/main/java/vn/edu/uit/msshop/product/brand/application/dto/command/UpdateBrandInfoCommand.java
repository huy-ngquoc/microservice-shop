package vn.edu.uit.msshop.product.brand.application.dto.command;

import org.jspecify.annotations.NullMarked;

import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.BrandName;
import vn.edu.uit.msshop.product.brand.domain.model.BrandVersion;
import vn.edu.uit.msshop.product.shared.application.dto.Change;

@NullMarked
public record UpdateBrandInfoCommand(
        BrandId id,
        Change<BrandName> name,
        BrandVersion expectedVersion) {
}
