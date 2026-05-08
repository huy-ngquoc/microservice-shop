package vn.uit.edu.msshop.order.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingInfoDocument {
    private String fullName;
    private String address;
    private String email;
    private String phone;
}
