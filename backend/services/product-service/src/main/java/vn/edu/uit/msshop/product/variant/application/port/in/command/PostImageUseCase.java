package vn.edu.uit.msshop.product.variant.application.port.in.command;

import java.io.IOException;

import vn.edu.uit.msshop.product.variant.application.dto.command.PostImageCommand;

public interface PostImageUseCase {
    public void postImage(PostImageCommand command) throws IOException;
}
