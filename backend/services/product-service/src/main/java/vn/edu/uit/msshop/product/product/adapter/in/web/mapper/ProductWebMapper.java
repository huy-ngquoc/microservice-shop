package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateProductRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateSimpleProductRequest;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductInfoRequest;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateSimpleProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.HardDeleteProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.ReorderImageCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.RestoreProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.SoftDeleteProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductInfoCommand;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTargets;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;
import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;

@Component
public class ProductWebMapper {
    public CreateProductCommand toCreateCommand(
            final CreateProductRequest request) {
        final var name = new ProductName(request.name());
        final var categoryId = new ProductCategoryId(request.categoryId());
        final var brandId = new ProductBrandId(request.brandId());

        final var options = ProductOptions.of(request.options());
        final var variantsList = request.variants().stream()
                .map(v -> NewProductVariant.of(
                        v.price(),
                        v.traits(),
                        v.targets()))
                .toList();
        final var newVariants = new NewProductVariants(variantsList);
        final var newConfiguration = new NewProductConfiguration(options, newVariants);

        return new CreateProductCommand(
                name,
                categoryId,
                brandId,
                newConfiguration);
    }

    public CreateSimpleProductCommand toCreateSimpleCommand(
            final CreateSimpleProductRequest request) {
        final var name = new ProductName(request.name());
        final var categoryId = new ProductCategoryId(request.categoryId());
        final var brandId = new ProductBrandId(request.brandId());
        final var price = new ProductVariantPrice(request.price());
        final var targets = ProductVariantTargets.of(request.targets());

        return new CreateSimpleProductCommand(
                name,
                categoryId,
                brandId,
                price,
                targets);
    }

    public RestoreProductCommand toRestoreCommand(
            final UUID id,
            final long expectedVersion) {
        final var productId = new ProductId(id);
        final var version = new ProductVersion(expectedVersion);

        return new RestoreProductCommand(productId, version);
    }

    public UpdateProductInfoCommand toUpdateInfoCommand(
            final UUID id,
            final UpdateProductInfoRequest request) {
        final var productId = new ProductId(id);
        final var version = new ProductVersion(request.expectedVersion());

        final var name = ChangeRequest.toChange(request.name(), ProductName::new);
        final var categoryId = ChangeRequest.toChange(request.categoryId(), ProductCategoryId::new);
        final var brandId = ChangeRequest.toChange(request.brandId(), ProductBrandId::new);

        return new UpdateProductInfoCommand(
                productId,
                name,
                categoryId,
                brandId,
                version);
    }

    public SoftDeleteProductCommand toSoftDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var productId = new ProductId(id);
        final var version = new ProductVersion(expectedVersion);

        return new SoftDeleteProductCommand(productId, version);
    }

    public HardDeleteProductCommand toHardDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var productId = new ProductId(id);
        final var version = new ProductVersion(expectedVersion);

        return new HardDeleteProductCommand(productId, version);
    }
    public ReorderImageCommand toReorderImageCommand(UUID productId, List<Integer> newIndexes, long version) {
        return new ReorderImageCommand(new ProductId(productId), newIndexes, version);
    }
}
