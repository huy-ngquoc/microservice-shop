package vn.uit.edu.msshop.inventory.adapter.in.web.request;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUpdatedInventoryRequest {
    private Instant startFirst;
    private Instant endFirst;
    private Instant startSecond;
    private Instant endSecond;
}
