package vn.uit.edu.msshop.image.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageFolder;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;
import vn.uit.edu.msshop.image.domain.model.valueobject.ObjectId;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemoveImageFolderCommand{
    private ImagePublicId publicId;
    private ImageFolder destination;
    private ObjectId objectId;
}
