package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductResponse;
import vn.edu.uit.msshop.product.product.adapter.in.web.response.ProductVariantResponse;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductVariantView;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

@Component
public class ProductResponseWebMapper {

    public ProductResponse toResponse(
            final ProductView view) {
        final var variantsList = new ArrayList<ProductVariantResponse>(view.variants().size());
        for (final var variantView : view.variants()) {
            final var variantResponse = ProductResponseWebMapper.toVariantResponse(variantView);
            variantsList.add(variantResponse);
        }

        return new ProductResponse(
                view.id(),
                view.name(),
                view.categoryId(),
                view.brandId(),
                view.minPrice(),
                view.maxPrice(),
                view.soldCount(),
                view.stockCount(),
                view.ratingTotal(),
                view.ratingCount(),
                view.options(),
                variantsList,
                view.imageKeys(),
                view.version());
    }

    private static ProductVariantResponse toVariantResponse(
            final ProductVariantView view) {
        return new ProductVariantResponse(
                view.id(),
                view.price(),
                view.traits());
    }
}
