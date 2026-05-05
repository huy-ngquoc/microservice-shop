package vn.edu.uit.msshop.product.product.adapter.in.web.request;

import org.jspecify.annotations.Nullable;

import jakarta.validation.constraints.NotNull;

public record RemoveProductOptionRequest(@Nullable Long defaultPrice,

@NotNull Long expectedVersion){}
