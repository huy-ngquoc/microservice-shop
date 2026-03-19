package vn.uit.edu.msshop.recommendation.application.dto.command;

import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantImages;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantName;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantTargets;

public record CreateVariantCommand(
    VariantId id,
    VariantImages images,
    VariantName name,
    VariantTargets targets
) {

}
