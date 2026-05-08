package vn.uit.edu.msshop.image.adapter.in.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
    private String url;
    private String publicId;
    private String fileName;
    private int width;
    private int height;
    
}
