package vn.edu.uit.msshop.product.application.dto.command;

import org.jspecify.annotations.NullMarked;

import vn.edu.uit.msshop.product.application.common.Change;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandId;
import vn.edu.uit.msshop.product.domain.model.brand.valueobject.BrandName;

@NullMarked
public record UpdateBrandInfoCommand(
        BrandId id,

        Change<BrandName> name) {
}
