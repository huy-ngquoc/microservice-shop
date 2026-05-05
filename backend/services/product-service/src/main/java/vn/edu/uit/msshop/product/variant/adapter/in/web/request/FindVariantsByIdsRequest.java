package vn.edu.uit.msshop.product.variant.adapter.in.web.request;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FindVariantsByIdsRequest(@Size(max=100)Set<@NotNull UUID>ids){}
