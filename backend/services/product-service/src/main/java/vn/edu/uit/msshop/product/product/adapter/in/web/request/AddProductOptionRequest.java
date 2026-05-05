package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTrait;

public record AddProductOptionRequest(@NotBlank @Size(max=ProductOption.MAX_RAW_LENGTH_VALUE)String option,

@NotBlank @Size(max=ProductVariantTrait.MAX_RAW_LENGTH)String defaultTrait,

@NotNull Long expectedVersion){}
