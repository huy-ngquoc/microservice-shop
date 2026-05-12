package vn.uit.edu.msshop.image.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.uit.edu.msshop.image.domain.model.valueobject.DataType;
import vn.uit.edu.msshop.image.domain.model.valueobject.ObjectId;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class ImageDeleted {
    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();
    private final Instant occurentTime = Instant.now();
    private final ObjectId objectId;
    private final DataType dataType;
}