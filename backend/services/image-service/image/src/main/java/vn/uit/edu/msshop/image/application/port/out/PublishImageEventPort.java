package vn.uit.edu.msshop.image.application.port.out;

import vn.uit.edu.msshop.image.domain.event.ImageRemoveSuccess;



public interface PublishImageEventPort {
    public void publishImageRemoveSuccess(ImageRemoveSuccess event);
}
