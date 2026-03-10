package vn.uit.edu.msshop.image.domain.event;

import java.time.Instant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
public class ProductImageEvent {
    private final Instant occurentTime = Instant.now();
    private ImagePublicId id;
}
