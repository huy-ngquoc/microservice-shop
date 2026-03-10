package vn.uit.edu.msshop.auth.domain.event;

public record AccountCreated(
    String id,
    String name, 
    String email,
    String password,
    String role,
    String status,
    String shippingAddress,
    String phoneNumber
) {

}
