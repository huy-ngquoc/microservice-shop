package vn.uit.edu.msshop.notification.application.port.in;

import vn.uit.edu.msshop.notification.domain.model.Email;

public interface SendNotificationUseCase {
    public void sendNotification( Email email);
}
