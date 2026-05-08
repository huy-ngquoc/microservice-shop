package vn.uit.edu.msshop.image.adapter.exception;

import java.io.IOException;

public class ImageUploadFailException extends RuntimeException {
    public ImageUploadFailException(IOException e) {
        super(e);
    }
}
