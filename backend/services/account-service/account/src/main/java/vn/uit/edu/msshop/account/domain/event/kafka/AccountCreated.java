package vn.uit.edu.msshop.account.domain.event.kafka;

import java.util.UUID;

public record AccountCreated(
    UUID id,
    String name, 
    String email,
    String password,
    String role,
    String status
) {

}
