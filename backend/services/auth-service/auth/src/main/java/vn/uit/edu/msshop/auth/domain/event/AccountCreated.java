package vn.uit.edu.msshop.auth.domain.event;

import java.util.UUID;

public record AccountCreated(
    UUID id,
    String name, 
    String email,
    String password,
    String role,
    String status) {

}
