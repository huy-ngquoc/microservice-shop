package vn.edu.uit.msshop.product.product.adapter.in.web.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByBrandIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByCategoryIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByVariantIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductSoftDeletedExistenceCheckByBrandIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductSoftDeletedExistenceCheckByCategoryIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.listing.ProductActiveListingQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.listing.ProductSoftDeletedListingQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.lookup.ProductActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.query.lookup.ProductSoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;

@Component
public class ProductQueryWebMapper {

    public ProductActiveExistenceCheckByIdQuery toActiveExistenceCheckByIdQuery(
            final UUID productId) {
        return new ProductActiveExistenceCheckByIdQuery(
                productId);
    }

    public ProductActiveExistenceCheckByBrandIdQuery toActiveExistenceCheckByBrandIdQuery(
            final UUID brandId) {
        return new ProductActiveExistenceCheckByBrandIdQuery(
                brandId);
    }

    public ProductActiveExistenceCheckByCategoryIdQuery toActiveExistenceCheckByCategoryIdQuery(
            final UUID categoryId) {
        return new ProductActiveExistenceCheckByCategoryIdQuery(
                categoryId);
    }

    public ProductActiveExistenceCheckByVariantIdQuery toActiveExistenceCheckByVariantIdQuery(
            final UUID variantId) {
        return new ProductActiveExistenceCheckByVariantIdQuery(
                variantId);
    }

    public ProductSoftDeletedExistenceCheckByBrandIdQuery toSoftDeletedExistenceCheckByBrandIdQuery(
            final UUID brandId) {
        return new ProductSoftDeletedExistenceCheckByBrandIdQuery(
                brandId);
    }

    public ProductSoftDeletedExistenceCheckByCategoryIdQuery toSoftDeletedExistenceCheckByCategoryIdQuery(
            final UUID categoryId) {
        return new ProductSoftDeletedExistenceCheckByCategoryIdQuery(
                categoryId);
    }

    public ProductActiveListingQuery toActiveListingQuery(
            final PageRequestDto pageRequest) {
        return new ProductActiveListingQuery(
                pageRequest);
    }

    public ProductSoftDeletedListingQuery toSoftDeletedListingQuery(
            final PageRequestDto pageRequest) {
        return new ProductSoftDeletedListingQuery(
                pageRequest);
    }

    public ProductActiveLookupByIdQuery toActiveLookupByIdQuery(
            UUID productId) {
        return new ProductActiveLookupByIdQuery(
                productId);
    }

    public ProductSoftDeletedLookupByIdQuery toSoftDeletedLookupByIdQuery(
            UUID productId) {
        return new ProductSoftDeletedLookupByIdQuery(
                productId);
    }
}
