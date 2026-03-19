package vn.uit.edu.msshop.recommendation.application.port.in;

import vn.uit.edu.msshop.recommendation.application.dto.command.UpdateVariantCommand;
import vn.uit.edu.msshop.recommendation.application.dto.query.VariantView;

public interface UpdateVariantUseCase {
    public VariantView update(UpdateVariantCommand command);
}
