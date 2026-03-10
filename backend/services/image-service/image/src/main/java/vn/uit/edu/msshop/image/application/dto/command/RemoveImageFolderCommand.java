package vn.uit.edu.msshop.image.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemoveImageFolderCommand{
    private ImagePublicId publicId;
}
