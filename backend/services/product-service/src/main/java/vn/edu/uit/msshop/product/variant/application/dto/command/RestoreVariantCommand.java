package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;

public record RestoreVariantCommand(VariantId id,VariantVersion expectedVersion){}
