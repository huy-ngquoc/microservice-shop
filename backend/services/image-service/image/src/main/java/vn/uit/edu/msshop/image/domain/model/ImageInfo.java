package vn.uit.edu.msshop.image.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageFileName;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageHeight;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageSize;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageUrl;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageWidth;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageInfo {
    private ImageUrl url;
    private ImagePublicId publicId;
    private ImageFileName fileName;
    private ImageWidth width;
    private ImageHeight height;
    private ImageSize size;
    
}
