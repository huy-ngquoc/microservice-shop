package vn.uit.edu.msshop.image.adapter.out.event.documents;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection="outbox_events_image_remove_success")
@Getter
@Setter
@Builder
public class ImageRemoveSuccessDocument {
    @Id
    private UUID eventId;
    private String url;
    private String publicId;
    private String fileName;
    private int width;
    private int height;
    private long size;
    private UUID objectId;
    private String eventStatus;
    private Integer retryCount; 
    private Instant createdAt;
    private Instant updatedAt; 
    private String lastError;
}
