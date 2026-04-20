package vn.uit.edu.msshop.notification.application.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.notification.application.dto.command.CreateEmailCommand;
import vn.uit.edu.msshop.notification.application.port.in.SendMailUseCase;
import vn.uit.edu.msshop.notification.application.port.out.LoadEmailPort;
import vn.uit.edu.msshop.notification.application.port.out.SaveEmailPort;
import vn.uit.edu.msshop.notification.domain.model.Email;
import vn.uit.edu.msshop.notification.domain.model.valueobject.CreationTime;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailContent;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailId;
import vn.uit.edu.msshop.notification.domain.model.valueobject.EmailStatus;
import vn.uit.edu.msshop.notification.domain.model.valueobject.SendTime;
import vn.uit.edu.msshop.notification.domain.model.valueobject.UpdateTime;

@Service
@RequiredArgsConstructor
public class SendEmailService implements SendMailUseCase{
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final LoadEmailPort loadPort;
    private final SaveEmailPort savePort;

    @Override
    public Email sendEmail(Email email) {
        if(email.getOrderInfo()==null) return sendTextEmail(email);
        return sendEmailWithOrder(email);
    }
    private Email sendTextEmail(Email email) {
    try {
        // 1. Tạo đối tượng SimpleMailMessage (chuyên cho văn bản thuần)
        SimpleMailMessage message = new SimpleMailMessage();

        // 2. Thiết lập các thông tin cơ bản
        // Giả sử bạn lấy email người nhận từ thông tin đơn hàng
        message.setTo(email.getUserEmail().value()); 
        message.setSubject(email.getEmailTitle().value());
        message.setText(email.getEmailContent().value()); // Nội dung lấy trực tiếp từ field
        message.setFrom("MSShop <your-email@gmail.com>");

        // 3. Thực hiện gửi
        mailSender.send(message);

        // 4. Cập nhật trạng thái thành công
        email.setEmailStatus(new EmailStatus("SENT"));
        email.setSendTime(new SendTime(Instant.now()));

    } catch (Exception e) {
        // 5. Xử lý khi gửi lỗi
        email.setEmailStatus(new EmailStatus("UNSENT"));
        e.printStackTrace();
    }
    
    // Trả về email để lưu vào MongoDB ở lớp gọi hàm này
    return email;
}
    private Email sendEmailWithOrder(Email email) {
        try {
            
            Context context = new Context();
            context.setVariable("order", email.getOrderInfo());

            
            String htmlContent = templateEngine.process("order-email", context);
            
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email.getUserEmail().value()); // Thay bằng email thực tế từ orderInfo nếu có
            helper.setSubject(email.getEmailTitle().value());
            helper.setText(htmlContent, true); // tham số true để xác định đây là HTML

            // 4. Thực hiện gửi
            mailSender.send(message);
            email.setEmailContent(new EmailContent(htmlContent));

           
            email.setEmailStatus(new EmailStatus("SENT"));
            email.setSendTime(new SendTime(Instant.now()));

        } catch (Exception e) {
            email.setEmailStatus(new EmailStatus("UNSENT"));
            // Log lỗi e.getMessage() nếu cần
        }
        return email;
    }

    @Override
    public Email createEmail(CreateEmailCommand command) {

        Email email = new Email(new EmailId(UUID.randomUUID()), command.getEmailContent(), command.getEmailStatus(), command.getEmailTitle(), command.getEmailType(), new SendTime(Instant.now()), new UpdateTime(null), command.getOrderInfo(), command.getUserEmail(), new CreationTime(Instant.now()));
        return savePort.save(sendEmail(email));
    }

    @Override
    @Scheduled(fixedRate=5000)
    public void sendFailedEmail() {
        List<Email> unsentEmail = loadPort.loadByEmailStatus(new EmailStatus("UNSENT"));
        List<Email> toSave = new ArrayList<>();
        for(Email email: unsentEmail) {
            toSave.add(sendEmail(email));
        }
        savePort.saveAll(toSave);
    }
}
