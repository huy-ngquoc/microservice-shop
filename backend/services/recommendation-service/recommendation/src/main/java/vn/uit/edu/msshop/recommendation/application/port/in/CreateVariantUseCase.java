package vn.uit.edu.msshop.recommendation.application.port.in;

import vn.uit.edu.msshop.recommendation.application.dto.command.CreateVariantCommand;
import vn.uit.edu.msshop.recommendation.application.dto.query.VariantView;

public interface CreateVariantUseCase {
    public VariantView create(CreateVariantCommand command);
}
