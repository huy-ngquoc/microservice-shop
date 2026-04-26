package vn.uit.edu.msshop.notification.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailContent;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailTitle;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailType;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.notification.domain.model.valueobject.UserEmail;
import vn.uit.edu.msshop.notification.domain.model.valueobject.UserId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmailCommand {
    private EmailContent emailContent;
    
    private EmailTitle emailTitle;
    private EmailType emailType;
    private OrderId orderId;
    private UserEmail userEmail;
    private UserId userId;
}
