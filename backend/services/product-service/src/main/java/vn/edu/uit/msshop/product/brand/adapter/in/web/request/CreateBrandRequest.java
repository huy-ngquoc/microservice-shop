package vn.edu.uit.msshop.product.brand.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandName;

public record CreateBrandRequest(@NotBlank @Size(max=BrandName.MAX_LENGTH)String name){}
