package vn.edu.uit.msshop.product.brand.application.dto.command;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;
import vn.edu.uit.msshop.shared.application.dto.Change;

public final class BrandLifecycleCommands {

    private BrandLifecycleCommands() {
    }

    public record Create(
            BrandName name) {
    }

    public record UpdateInfo(
            BrandId id,
            Change<BrandName> name,
            BrandVersion expectedVersion) {
    }

    public record SoftDelete(
            BrandId id,
            BrandVersion expectedVersion) {
    }

    public record Restore(
            BrandId id,
            BrandVersion expectedVersion) {
    }

    public record HardDelete(
            BrandId id,
            BrandVersion expectedVersion) {
    }
}
