package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.request.CreateProductRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.request.UpdateProductInfoRequest;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductVariantResponse;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.CreateSimpleProductCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductInfoCommand;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductVariantView;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.domain.model.NewProductConfiguration;
import vn.edu.uit.msshop.product.product.domain.model.NewProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;
import vn.edu.uit.msshop.product.product.domain.model.ProductCategoryId;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductName;
import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.product.product.domain.model.ProductPrice;
import vn.edu.uit.msshop.product.product.domain.model.ProductVersion;
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
                .map(v -> NewProductVariant.of(v.price(), v.traits()))
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
            final CreateProductRequest request) {
        final var name = new ProductName(request.name());
        final var categoryId = new ProductCategoryId(request.categoryId());
        final var brandId = new ProductBrandId(request.brandId());

        final var rawPrice = Objects.requireNonNull(request.price(),
                "Price must not be null for simple product");
        final var price = new ProductPrice(rawPrice);

        return new CreateSimpleProductCommand(
                name,
                categoryId,
                brandId,
                price);
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
                view.ratingAverage(),
                view.ratingCount(),
                view.options(),
                variantsList,
                view.imageKeys());
    }

    public ProductVariantResponse toVariantResponse(
            final ProductVariantView view) {
        return new ProductVariantResponse(
                view.id(),
                view.price(),
                view.traits());
    }
}
