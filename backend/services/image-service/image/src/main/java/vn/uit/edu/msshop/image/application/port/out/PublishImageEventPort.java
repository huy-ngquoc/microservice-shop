package vn.uit.edu.msshop.image.application.port.out;


import vn.uit.edu.msshop.image.adapter.out.event.documents.ImageRemoveSuccessDocument;



public interface PublishImageEventPort {
    public void publishImageRemoveSuccess(ImageRemoveSuccessDocument outboxEvent);
}
