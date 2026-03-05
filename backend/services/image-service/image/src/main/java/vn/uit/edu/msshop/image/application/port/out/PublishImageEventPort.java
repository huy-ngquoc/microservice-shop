package vn.uit.edu.msshop.image.application.port.out;

import vn.uit.edu.msshop.image.domain.event.ImageDeleted;
import vn.uit.edu.msshop.image.domain.event.ImageUpdated;

public interface PublishImageEventPort {
    public void publish(ImageUpdated imageUpdated);
    public void publish(ImageDeleted imageDeleted);
}
