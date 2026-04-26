package vn.uit.edu.msshop.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.edu.msshop.notification.domain.model.valueobject.CreationTime;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailContent;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailId;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailStatus;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailTitle;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailType;
import vn.uit.edu.msshop.notification.domain.model.valueobject.OrderId;
import vn.uit.edu.msshop.notification.domain.model.valueobject.SendTime;
import vn.uit.edu.msshop.notification.domain.model.valueobject.UpdateTime;
import vn.uit.edu.msshop.notification.domain.model.valueobject.UserEmail;
import vn.uit.edu.msshop.notification.domain.model.valueobject.UserId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private EmailId emailId;
    private EmailContent emailContent;
    private EmailStatus emailStatus;
    private EmailTitle emailTitle;
    private EmailType emailType;
    private SendTime sendTime;
    private UpdateTime updateTime;
    private OrderId orderId;
    private UserEmail userEmail;
    private CreationTime creationTime;
    private UserId userId;
}
