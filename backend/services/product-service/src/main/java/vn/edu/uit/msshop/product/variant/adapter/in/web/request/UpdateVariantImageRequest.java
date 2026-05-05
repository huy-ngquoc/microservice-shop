package vn.edu.uit.msshop.product.variant.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;

public record UpdateVariantImageRequest(@NotBlank @Size(max=VariantImageKey.MAX_LENGTH)String newImageKey,

@NotNull Long version){}
