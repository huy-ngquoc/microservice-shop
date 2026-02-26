package vn.edu.uit.msshop.product.domain.model.account.event;

import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.domain.model.account.valueobject.AccountId;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
public class AccountUpdate {
    @EqualsAndHashCode.Include
    private final UUID eventId = UUID.randomUUID();
    private final Instant occurentTime = Instant.now();
    private final AccountId accountId ;
}
