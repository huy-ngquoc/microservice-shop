package vn.uit.edu.msshop.image.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageRemoveSuccess {
    private UUID eventId;
    private String url;
    private String publicId;
    private String fileName;
    private int width;
    private int height;
    private long size;
    private UUID objectId;
}
