package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductVariantRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.AddProductVariantsRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateProductRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateSimpleProductRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.RemoveProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductInfoRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductOptionRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductVariantResponse;
import vn.edu.uit.msshop.product.product.application.dto.command.AddProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.AddProductVariantsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateSimpleProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.HardDeleteProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.RemoveProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.RestoreProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.SoftDeleteProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductInfoCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductOptionCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductVariantView;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTargets;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTrait;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;
import vn.edu.uit.msshop.product.shared.adapter.in.web.request.ChangeRequest;

@Component
public class ProductWebMapper {
    public CreateProductCommand toCreateCommand(
            final CreateProductRequest request) {
        final var name = new ProductName(request.name());
        final var categoryId = new ProductCategoryId(request.categoryId());
        final var brandId = new ProductBrandId(request.brandId());

        final var options = ProductOptions.of(request.options());
        final var variantsList = request.variants().stream()
                .map(v -> NewProductVariant.of(v.price(), v.traits(), v.targets()))
                .toList();
        final var newVariants = new NewProductVariants(variantsList);
        final var newConfiguration = new NewProductConfiguration(
                options,
                newVariants);

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

        return new RestoreProductCommand(
                productId,
                version);
    }

    public AddProductOptionCommand toAddOptionCommand(
            final UUID id,
            final AddProductOptionRequest request) {
        final var productId = new ProductId(id);
        final var option = new ProductOption(request.option());
        final var defaultTrait = new ProductVariantTrait(request.defaultTrait());
        final var version = new ProductVersion(request.expectedVersion());

        return new AddProductOptionCommand(
                productId,
                option,
                defaultTrait,
                version);
    }

    public AddProductVariantsCommand toAddVariantsCommand(
            final UUID id,
            final AddProductVariantRequest request) {
        final var productId = new ProductId(id);
        final var version = new ProductVersion(request.expectedVersion());

        final var variantPrice = new ProductVariantPrice(request.price());
        final var variantTraits = ProductVariantTraits.of(request.traits());
        final var variantTargets = ProductVariantTargets.of(request.targets());
        final var newVariant = new NewProductVariant(
                variantPrice,
                variantTraits,
                variantTargets);
        final var newVariants = new NewProductVariants(List.of(newVariant));

        return new AddProductVariantsCommand(
                productId,
                newVariants,
                version);
    }

    public AddProductVariantsCommand toAddVariantsCommand(
            final UUID id,
            final AddProductVariantsRequest request) {
        final var productId = new ProductId(id);
        final var version = new ProductVersion(request.expectedVersion());

        final var newVariantsList = request.variants().stream()
                .map(this::toNewVariant)
                .toList();
        final var newVariants = new NewProductVariants(newVariantsList);

        return new AddProductVariantsCommand(
                productId,
                newVariants,
                version);
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

    public UpdateProductOptionCommand toUpdateOptionCommand(
            final UUID id,
            final int index,
            final UpdateProductOptionRequest request) {
        final var productId = new ProductId(id);
        final var option = new ProductOption(request.option());
        final var version = new ProductVersion(request.expectedVersion());

        return new UpdateProductOptionCommand(
                productId,
                index,
                option,
                version);
    }

    public RemoveProductOptionCommand toRemoveOptionCommand(
            final UUID id,
            final int index,
            final RemoveProductOptionRequest request) {
        final var productId = new ProductId(id);
        final var defaultPrice = ProductPrice.ofNullable(request.defaultPrice());
        final var version = new ProductVersion(request.expectedVersion());

        return new RemoveProductOptionCommand(
                productId,
                index,
                defaultPrice,
                version);
    }

    public SoftDeleteProductCommand toSoftDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var productId = new ProductId(id);
        final var version = new ProductVersion(expectedVersion);

        return new SoftDeleteProductCommand(
                productId,
                version);
    }

    public HardDeleteProductCommand toHardDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var productId = new ProductId(id);
        final var version = new ProductVersion(expectedVersion);

        return new HardDeleteProductCommand(
                productId,
                version);
    }

    public ProductId toProductId(
            final UUID id) {
        return new ProductId(id);
    }

    public ProductResponse toResponse(
            final ProductView view) {
        final var variantsList = view.variants().stream()
                .map(this::toVariantResponse).toList();

        return new ProductResponse(
                view.id(),
                view.name(),
                view.categoryId(),
                view.brandId(),
                view.minPrice(),
                view.maxPrice(),
                view.soldCount(),
                view.stockCount(),
                view.ratingAverage(),
                view.ratingCount(),
                view.options(),
                variantsList,
                view.imageKeys(),
                view.version());
    }

    public ProductVariantResponse toVariantResponse(
            final ProductVariantView view) {
        return new ProductVariantResponse(
                view.id(),
                view.price(),
                view.traits());
    }

    private NewProductVariant toNewVariant(
            final AddProductVariantsRequest.ProductVariantRequest request) {
        final var variantPrice = new ProductVariantPrice(request.price());
        final var variantTraits = ProductVariantTraits.of(request.traits());
        final var variantTargets = ProductVariantTargets.of(request.targets());

        return new NewProductVariant(
                variantPrice,
                variantTraits,
                variantTargets);
    }
}
