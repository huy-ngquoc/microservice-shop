package vn.uit.edu.msshop.account.application.dto.command;

import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AvatarPublicId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AvatarUrl;
import vn.uit.edu.msshop.account.domain.model.valueobject.ImageSize;

public record UpdateAvatarCommand(AvatarUrl url,AvatarPublicId publicId, ImageSize imageSize,AccountId id ) {

}
