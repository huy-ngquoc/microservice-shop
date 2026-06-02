package vn.edu.uit.msshop.product.category.application.dto.command;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryName;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryVersion;
import vn.edu.uit.msshop.shared.application.dto.Change;

public final class CategoryLifecycleCommands {

    private CategoryLifecycleCommands() {
    }

    public record Create(
            CategoryName name) {
    }

    public record UpdateInfo(
            CategoryId id,
            Change<CategoryName> name,
            CategoryVersion expectedVersion) {
    }

    public record SoftDelete(
            CategoryId id,
            CategoryVersion expectedVersion) {
    }

    public record Restore(
            CategoryId id,
            CategoryVersion expectedVersion) {
    }

    public record HardDelete(
            CategoryId id,
            CategoryVersion expectedVersion) {
    }
}
