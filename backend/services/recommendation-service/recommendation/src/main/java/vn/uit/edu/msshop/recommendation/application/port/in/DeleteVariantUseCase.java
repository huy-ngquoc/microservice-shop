package vn.uit.edu.msshop.recommendation.application.port.in;

import vn.uit.edu.msshop.recommendation.application.dto.command.DeleteVariantCommand;

public interface DeleteVariantUseCase {
    public void delete(DeleteVariantCommand command);
}
