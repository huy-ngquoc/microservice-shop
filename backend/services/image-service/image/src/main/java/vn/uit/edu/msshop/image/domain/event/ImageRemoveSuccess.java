package vn.uit.edu.msshop.image.domain.event;

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
    private int size;
}
