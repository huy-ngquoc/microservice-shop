package vn.uit.edu.msshop.notification.application.port.in;

import vn.uit.edu.msshop.notification.application.dto.command.CreateEmailCommand;
import vn.uit.edu.msshop.notification.domain.model.Email;

public interface SendMailUseCase {
    public Email sendEmail(Email email);
    public Email createEmail(CreateEmailCommand command);
    public void sendFailedEmail();
}
