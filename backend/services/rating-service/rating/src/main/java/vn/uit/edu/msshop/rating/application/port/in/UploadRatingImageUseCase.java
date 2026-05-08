package vn.uit.edu.msshop.rating.application.port.in;



import vn.uit.edu.msshop.rating.application.dto.command.UploadRatingImageCommand;
import vn.uit.edu.msshop.rating.application.dto.query.ImageView;

public interface UploadRatingImageUseCase {
    public ImageView uploadRatingImage(UploadRatingImageCommand command);
}
