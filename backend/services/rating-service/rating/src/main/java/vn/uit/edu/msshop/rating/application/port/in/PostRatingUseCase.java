package vn.uit.edu.msshop.rating.application.port.in;

import vn.uit.edu.msshop.rating.application.dto.command.PostRatingCommand;

public interface PostRatingUseCase {
    public void post(PostRatingCommand command);
}
