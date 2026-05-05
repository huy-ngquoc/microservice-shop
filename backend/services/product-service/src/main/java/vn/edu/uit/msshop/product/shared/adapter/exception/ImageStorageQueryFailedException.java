package vn.edu.uit.msshop.product.shared.adapter.exception;

public final class ImageStorageQueryFailedException extends RuntimeException {
  private static final String DEFAULT_MESSAGE = "Query image storage failed";

  public ImageStorageQueryFailedException() {
    super(DEFAULT_MESSAGE);
  }

  public ImageStorageQueryFailedException(final Exception exception) {
    super(DEFAULT_MESSAGE, exception);
  }

  public ImageStorageQueryFailedException(final String msg, final Exception exception) {
    super(msg, exception);
  }
}
