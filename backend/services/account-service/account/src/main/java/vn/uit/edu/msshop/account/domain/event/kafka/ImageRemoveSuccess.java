package vn.uit.edu.msshop.account.domain.event.kafka;
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
    private String url;
    private String publicId;
    private String fileName;
    private int width;
    private int height;
    private long size;
    private UUID objectId;
}
