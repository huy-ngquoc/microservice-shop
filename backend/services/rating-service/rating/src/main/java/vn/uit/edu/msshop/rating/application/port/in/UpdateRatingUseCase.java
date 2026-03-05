package vn.uit.edu.msshop.rating.application.port.in;

import vn.uit.edu.msshop.rating.application.dto.command.UpdateRatingCommand;

public interface UpdateRatingUseCase {
    public void update(UpdateRatingCommand command);
}
