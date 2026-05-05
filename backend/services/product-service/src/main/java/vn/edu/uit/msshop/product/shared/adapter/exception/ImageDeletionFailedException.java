package vn.edu.uit.msshop.product.shared.adapter.exception;

public final class ImageDeletionFailedException extends RuntimeException {
  private static final String DEFAULT_MESSAGE = "Delete image failed";

  public ImageDeletionFailedException() {
    super(DEFAULT_MESSAGE);
  }

  public ImageDeletionFailedException(final Exception exception) {
    super(DEFAULT_MESSAGE, exception);
  }

  public ImageDeletionFailedException(final String msg, final Exception exception) {
    super(msg, exception);
  }
}
