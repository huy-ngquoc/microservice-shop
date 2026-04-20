package vn.uit.edu.msshop.notification.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.edu.msshop.notification.domain.model.OrderInfo;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailContent;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailStatus;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailTitle;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailType;
import vn.uit.edu.msshop.notification.domain.model.valueobject.UserEmail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmailCommand {
    private EmailContent emailContent;
    private EmailStatus emailStatus;
    private EmailTitle emailTitle;
    private EmailType emailType;
    private OrderInfo orderInfo;
    private UserEmail userEmail;

}
