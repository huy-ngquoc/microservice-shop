package vn.uit.edu.msshop.notification.application.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.notification.application.dto.command.CreateEmailCommand;
import vn.uit.edu.msshop.notification.application.port.in.SendMailUseCase;
import vn.uit.edu.msshop.notification.application.port.out.LoadEmailPort;
import vn.uit.edu.msshop.notification.application.port.out.SaveEmailPort;
import vn.uit.edu.msshop.notification.domain.model.Email;
import vn.uit.edu.msshop.notification.domain.model.valueobject.CreationTime;
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
        return sendTextEmail(email);
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
        message.setFrom("leyen15121971@gmail.com");

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
    
    @Override
    public Email createEmail(CreateEmailCommand command) {

        Email email = new Email(new EmailId(UUID.randomUUID()), command.getEmailContent(), new EmailStatus("UNSENT"), command.getEmailTitle(), command.getEmailType(), new SendTime(Instant.now()), new UpdateTime(null), command.getOrderId(), command.getUserEmail(), new CreationTime(Instant.now()), command.getUserId());
        return savePort.save(sendEmail(email));
    }

    @Override
    //@Scheduled(fixedRate=5000)
    public void sendFailedEmail() {
        List<Email> unsentEmail = loadPort.loadByEmailStatus(new EmailStatus("UNSENT"));
        List<Email> toSave = new ArrayList<>();
        for(Email email: unsentEmail) {
            toSave.add(sendEmail(email));
        }
        savePort.saveAll(toSave);
    }
}
