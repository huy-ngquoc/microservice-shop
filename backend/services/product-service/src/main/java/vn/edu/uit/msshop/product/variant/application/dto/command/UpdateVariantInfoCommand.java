package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.shared.application.dto.Change;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;

public record UpdateVariantInfoCommand(VariantId id,Change<VariantPrice>price,Change<VariantTraits>traits,Change<VariantTargets>targets,VariantVersion expectedVersion){}
