package vn.uit.edu.msshop.account.domain.event.normal;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public class AccountUpdate {
    @EqualsAndHashCode.Include
    private final UUID eventId = UUIDs.newId();
    private final Instant occurentTime = Instant.now();
    private final AccountId accountId;
}
