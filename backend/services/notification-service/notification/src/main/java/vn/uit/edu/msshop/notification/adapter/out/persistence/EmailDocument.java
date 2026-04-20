package vn.uit.edu.msshop.notification.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="email_document")
public class EmailDocument {
    @Id
    private UUID emailId;
    private String emailContent;
    private String emailStatus;
    private String emailTitle;
    private String emailType;
    private UUID orderId;
    private String userEmail;
    @Indexed(expireAfter="2592000")
    private Instant creationTime;
    private Instant sendTime;
    @LastModifiedDate
    private Instant updateTime;

   
}
